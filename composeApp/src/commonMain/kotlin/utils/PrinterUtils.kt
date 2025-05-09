import com.github.anastaciocintra.escpos.EscPos
import com.github.anastaciocintra.escpos.Style
import com.github.anastaciocintra.output.PrinterOutputStream
import javax.print.PrintServiceLookup
import kotlin.math.roundToInt

fun printText(
    printerName: String?,
    text: String,
    widthMmStr: String,
    fontSizePtStr: String,
    marginMmStr: String,
) {
    val service = PrintServiceLookup.lookupPrintServices(null, null)
        .firstOrNull { it.name.equals(printerName, ignoreCase = true) }
        ?: error("Impressora '$printerName' não encontrada")

    val widthMm       = widthMmStr.toFloatOrNull()     ?: 58f
    val fontSizePt    = fontSizePtStr.toFloatOrNull()  ?: 10f
    val marginMm      = marginMmStr.toFloatOrNull()    ?: 0f


    val cols         = if (widthMm >= 70f) 48 else 32
    val marginCols   = ((marginMm / widthMm) * cols).roundToInt().coerceAtMost(cols/4)
    val indent       = " ".repeat(marginCols)

    val fontSize = when {
        fontSizePt <= 11f -> Style.FontSize._1
        fontSizePt <= 17f -> Style.FontSize._2
        fontSizePt <= 23f -> Style.FontSize._3
        fontSizePt <= 29f -> Style.FontSize._4
        fontSizePt <= 35f -> Style.FontSize._5
        fontSizePt <= 41f -> Style.FontSize._6
        fontSizePt <= 47f -> Style.FontSize._7
        else              -> Style.FontSize._8
    }


    PrinterOutputStream(service).use { posStream ->
        EscPos(posStream).use { esc ->
            esc.initializePrinter()
            esc.setStyle(Style().setFontSize(fontSize, fontSize))
            text.lines().forEach { line ->
                posStream.write((indent + line +  "\n").toByteArray(Charsets.UTF_8))
            }
            esc.feed(5).cut(EscPos.CutMode.FULL);

        }
    }

}
