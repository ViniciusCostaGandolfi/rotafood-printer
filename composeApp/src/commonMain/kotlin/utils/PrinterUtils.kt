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
    widthMmStr: String,
    fontSizePtStr: String,
    marginMmStr: String,
    spacingPtStr: String
) {
    // 1) Nome da impressora não pode ser nulo
    val name = printerName ?: run {
        println("❌ printerName é nulo"); return
    }

    // 2) Converte String → Float/Int com defaults
    val widthMm    = widthMmStr.toFloatOrNull()    ?: 58f
    val fontSizePt = fontSizePtStr.toFloatOrNull() ?: 10f
    val marginMm   = marginMmStr.toFloatOrNull()   ?: 0f
    val spacingPt  = spacingPtStr.toIntOrNull()
        ?.coerceIn(1, 255)                        // 1 ≤ n ≤ 255
        ?: 3                                      // fallback mínimo

    // 3) Pega o serviço (usa BYTE_ARRAY.AUTOSENSE)
    val service = try {
        PrinterOutputStream.getPrintServiceByName(name)
    } catch (e: IllegalArgumentException) {
        println("❌ Impressora \"$name\" não encontrada"); return
    }

    PrinterOutputStream(service).use { out ->
        EscPos(out).use { esc ->
            esc.initializePrinter()
            esc.setCharacterCodeTable(EscPos.CharacterCodeTable.CP850_Multilingual)

            val cols = if (widthMm >= 70f) 48 else 32
            val marginSpaces = ((marginMm / widthMm) * cols)
                .toInt()
                .coerceAtMost(cols / 4)
            val indent = " ".repeat(marginSpaces)
            val maxLine = cols - marginSpaces

            val wrapped = text.lines().flatMap { raw ->
                raw.split(' ').fold(mutableListOf<String>()) { acc, w ->
                    if (acc.isEmpty() || acc.last().length + 1 + w.length > maxLine) {
                        acc.add(w)
                    } else {
                        acc[acc.lastIndex] = acc.last() + " $w"
                    }
                    acc
                }
            }

            val fontSize = if (fontSizePt >= 24f)
                Style.FontSize._2
            else
                Style.FontSize._1
            val style = Style().setFontSize(fontSize, fontSize)

            wrapped.forEach { esc.writeLF(style, indent + it) }

            esc.feed(spacingPt)
            esc.cut(EscPos.CutMode.FULL)
        }
    }
}
