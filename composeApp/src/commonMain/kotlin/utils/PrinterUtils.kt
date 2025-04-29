package utils

import org.apache.pdfbox.pdmodel.*
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDType1Font
import javax.print.DocFlavor
import javax.print.PrintServiceLookup
import javax.print.SimpleDoc


fun printText(
    printerName: String?,
    text: String,
    widthMm: String,
    heightMm: String,
    fontSizePt: String,
    marginMm: String,
    spacingPt: String
) {
    val printerService = PrintServiceLookup.lookupPrintServices(null, null)
        .firstOrNull { it.name == printerName } ?: run {
        println("Impressora \"$printerName\" não encontrada."); return
    }

    val wPt  = (widthMm .toFloatOrNull() ?: 58f) / 25.4f * 72f
    val hPt  = (heightMm.toFloatOrNull() ?: 80f) / 25.4f * 72f
    val mPt  = (marginMm.toFloatOrNull() ?: 5f)  / 25.4f * 72f
    val fPt  = fontSizePt.toFloatOrNull() ?: 10f
    val extra = spacingPt.toFloatOrNull() ?: 2f
    val lineH = (fPt * 1.2f) + extra

    val availW          = wPt - 2 * mPt
    val avgCharW        = fPt * 0.6f
    val maxChars        = (availW / avgCharW).toInt()
    val indent          = "   "
    val processedLines  = buildList {
        for (raw in text.split('\n')) {
            var cur = ""
            for (word in raw.split(' ')) {
                if (cur.isEmpty()) cur = word
                else if (cur.length + 1 + word.length <= maxChars) cur += " $word"
                else { add(cur); cur = indent + word }
            }
            if (cur.isNotEmpty()) add(cur)
        }
    }

    val usableH = hPt - 2 * mPt
    val maxLinesPerPage = (usableH / lineH).toInt()

    val doc = PDDocument()
    var idx = 0
    while (idx < processedLines.size) {
        val page = PDPage(PDRectangle(wPt, hPt)); doc.addPage(page)
        PDPageContentStream(doc, page).use { cs ->
            cs.beginText()
            cs.setFont(PDType1Font.COURIER, fPt)
            cs.setLeading(lineH)
            cs.newLineAtOffset(mPt, page.mediaBox.height - mPt)
            for (i in idx until (idx + maxLinesPerPage).coerceAtMost(processedLines.size)) {
                if (i != idx) cs.newLine()
                cs.showText(processedLines[i])
            }
            cs.endText()
        }
        idx += maxLinesPerPage
    }

    val bytes = java.io.ByteArrayOutputStream().also { doc.save(it) }.toByteArray()
    doc.close()

    printerService.createPrintJob()
        .print(SimpleDoc(bytes, DocFlavor.BYTE_ARRAY.AUTOSENSE, null), null)
}
