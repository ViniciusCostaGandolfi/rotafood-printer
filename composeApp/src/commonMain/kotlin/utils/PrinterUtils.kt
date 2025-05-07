import com.github.anastaciocintra.escpos.EscPos
import com.github.anastaciocintra.escpos.Style
import com.github.anastaciocintra.output.PrinterOutputStream
import javax.print.PrintServiceLookup

private fun ptsToFontSize(pts: Float): Style.FontSize = when (pts.toInt()) {
    in  0..11 -> Style.FontSize._1
    in 12..17 -> Style.FontSize._2
    in 18..23 -> Style.FontSize._3
    in 24..29 -> Style.FontSize._4
    in 30..35 -> Style.FontSize._5
    in 36..41 -> Style.FontSize._6
    in 42..47 -> Style.FontSize._7
    else      -> Style.FontSize._8
}

fun printText(
    printerName: String?,
    text: String,
    widthMm: String,
    fontSizePt: String,
    marginMm: String,
    spacingPt: String
) {
    val service = PrintServiceLookup.lookupPrintServices(null, null)
        .firstOrNull { it.name == printerName }
        ?: run {
            println("Impressora \"$printerName\" não encontrada."); return
        }

    val colPerLine = when ((widthMm.toFloatOrNull() ?: 58f).toInt()) {
        in 70..79 -> 48 else -> 32
    }
    val marginSpaces = ((marginMm.toFloatOrNull() ?: 0f) / 58f * colPerLine).toInt()
        .coerceAtMost(colPerLine / 4)

    val indent  = " ".repeat(marginSpaces)
    val maxLine = colPerLine - marginSpaces

    val wrapped = buildList {
        text.split('\n').forEach { raw ->
            var cur = ""
            raw.split(' ').forEach { word ->
                if (cur.isEmpty()) cur = word
                else if (cur.length + 1 + word.length <= maxLine) cur += " $word"
                else { add(cur); cur = word }
            }
            if (cur.isNotBlank()) add(cur)
        }
    }

    PrinterOutputStream(service).use { stream ->
        EscPos(stream).use { esc ->
            val size = ptsToFontSize(fontSizePt.toFloatOrNull() ?: 10f)
            val style = Style().setFontSize(size, size)

            spacingPt.toFloatOrNull()?.let { esc.feed(it.toInt()) }

            for (line in wrapped) esc.write(style, indent + line).feed(1)

            esc.feed(3).cut(EscPos.CutMode.FULL)
        }
    }
}
