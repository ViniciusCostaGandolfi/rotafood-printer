package utils



//import com.github.anastaciocintra.escpos.EscPos
//import com.github.anastaciocintra.escpos.EscPosConst
//import com.github.anastaciocintra.escpos.Style
//import com.github.anastaciocintra.output.PrinterOutputStream
//
//
//private fun ptsToFontSize(pts: Float): Style.FontSize = when (pts.toInt()) {
//    in  0..11 -> Style.FontSize._1
//    in 12..17 -> Style.FontSize._2
//    in 18..23 -> Style.FontSize._3
//    in 24..29 -> Style.FontSize._4
//    in 30..35 -> Style.FontSize._5
//    in 36..41 -> Style.FontSize._6
//    in 42..47 -> Style.FontSize._7
//    else      -> Style.FontSize._8
//}
//
//
//
//
//fun printText(
//    printerName: String?,
//    text: String,
//    widthMmStr: String,
//    fontSizePtStr: String,
//    marginMmStr: String,
//    spacingPtStr: String
//) {
//    val name = printerName ?: run {
//        println("❌ printerName é nulo"); return
//    }
//
//    val widthMm    = widthMmStr.toFloatOrNull()    ?: 58f
//    val fontSizePt = fontSizePtStr.toFloatOrNull() ?: 10f
//    val marginMm   = marginMmStr.toFloatOrNull()   ?: 0f
//    val spacingPt  = spacingPtStr.toIntOrNull()
//        ?.coerceIn(1, 255)
//        ?: 3
//
//    val service = try {
//        PrinterOutputStream.getPrintServiceByName(name)
//    } catch (e: IllegalArgumentException) {
//        println("❌ Impressora \"$name\" não encontrada"); return
//    }
//
//
//    PrinterOutputStream(service).use { out ->
//        EscPos(out).use { esc ->
//            esc.initializePrinter()
//            esc.setCharacterCodeTable(EscPos.CharacterCodeTable.CP850_Multilingual)
//
//            val cols = if (widthMm >= 70f) 48 else 32
//            val marginSpaces = ((marginMm / widthMm) * cols)
//                .toInt()
//                .coerceAtMost(cols / 4)
//            val indent = " ".repeat(marginSpaces)
//            val maxLine = cols - marginSpaces
//
//            val wrapped = text.lines().flatMap { raw ->
//                raw.split(' ').fold(mutableListOf<String>()) { acc, w ->
//                    if (acc.isEmpty() || acc.last().length + 1 + w.length > maxLine) {
//                        acc.add(w)
//                    } else {
//                        acc[acc.lastIndex] = acc.last() + " $w"
//                    }
//                    acc
//                }
//            }
//
//            val fontSizeEnum = if (fontSizePt >= 24f) Style.FontSize._2 else Style.FontSize._1
//            val style = Style()
//                .setFontSize(fontSizeEnum, fontSizeEnum)
//                .setJustification(EscPosConst.Justification.Left_Default)
//
//            esc.setStyle(style)
//
//            wrapped.forEach { esc.writeLF(style, indent + it) }
//
//            esc.feed(spacingPt)
//            esc.cut(EscPos.CutMode.FULL)
//        }
//    }
//}

//
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDType1Font
import java.io.ByteArrayOutputStream
import javax.print.DocFlavor
import javax.print.PrintServiceLookup
import javax.print.SimpleDoc


//fun printText(
//    printerName: String?,
//    text: String,
//    widthMm: String,
//    fontSizePt: String,
//    marginMm: String,
//    lineSpacingPt: String
//) {
//    val serviceName = printerName?.trim().takeUnless { it.isNullOrEmpty() }
//        ?: error("Nome da impressora não informado")
//
//    val widthMmVal   = widthMm.toFloatOrNull()?.takeIf { it > 0 } ?: 58f
//    val fontSizeVal  = fontSizePt.toIntOrNull()?.takeIf { it > 0 } ?: 10
//    val marginMmVal  = marginMm.toFloatOrNull()?.coerceAtLeast(0f) ?: 0f
//    val lineSpaceVal = lineSpacingPt.toIntOrNull()?.coerceAtLeast(0) ?: 2
//
//    fun mmToPt(mm: Float) = mm / 25.4f * 72f
//    val widthPt   = mmToPt(widthMmVal)
//    val marginPt  = mmToPt(marginMmVal)
//    val leading   = fontSizeVal + lineSpaceVal
//
//    val lines     = text.lines()
//    val heightPt  = leading * lines.size + 2 * marginPt
//    val pageRect  = PDRectangle(widthPt, heightPt)
//
//    val doc  = PDDocument()
//    val page = PDPage(pageRect).also { doc.addPage(it) }
//
//    PDPageContentStream(doc, page).use { cs ->
//        cs.beginText()
//        cs.setFont(PDType1Font.COURIER, fontSizeVal.toFloat())
//        cs.newLineAtOffset(marginPt, heightPt - marginPt - fontSizeVal)
//
//        lines.forEach {
//            cs.showText(it)
//            cs.newLineAtOffset(0f, -leading.toFloat())
//        }
//        cs.endText()
//    }
//
//    val pdfBytes = ByteArrayOutputStream().also { doc.save(it) }.toByteArray()
//    doc.close()
//
//    val printer = PrintServiceLookup.lookupPrintServices(null, null)
//        .firstOrNull { it.name.equals(serviceName, true) }
//        ?: error("Impressora \"$serviceName\" não encontrada")
//
//    printer.createPrintJob()
//        .print(SimpleDoc(pdfBytes, DocFlavor.BYTE_ARRAY.AUTOSENSE, null), null)
//}
