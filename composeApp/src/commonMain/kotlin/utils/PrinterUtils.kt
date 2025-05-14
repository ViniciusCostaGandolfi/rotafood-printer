import com.github.anastaciocintra.escpos.EscPos
import com.github.anastaciocintra.escpos.Style
import com.github.anastaciocintra.output.PrinterOutputStream
import io.ktor.utils.io.charsets.*
import javax.print.PrintServiceLookup
import kotlin.math.roundToInt


fun printText(
    printerName: String?,
    text: String,
    widthMmStr: String,
    marginMmStr: String,
) {


    val service = PrintServiceLookup.lookupPrintServices(null, null)
        .firstOrNull { it.name.equals(printerName, ignoreCase = true) }
        ?: error("Impressora '$printerName' não encontrada")

    val widthMm       = widthMmStr.toFloatOrNull()     ?: 58f
    val marginMm      = marginMmStr.toFloatOrNull()    ?: 0f


    val cols         = if (widthMm >= 70f) 48 else 32
    val marginCols   = ((marginMm / widthMm) * cols).roundToInt().coerceAtMost(cols/4)
    val indent       = " ".repeat(marginCols)


    PrinterOutputStream(service).use { posStream ->
        EscPos(posStream).use { esc ->
            esc.initializePrinter()
            esc.setPrinterCharacterTable(EscPos.CharacterCodeTable.CP860_Portuguese.value)
            esc.setStyle(
                Style()
                    .setFontSize(Style.FontSize._1, Style.FontSize._1)
                    .setBold(true)
            )
            val cpCharset = Charset.forName("CP860")
            text.lines().forEach { line ->
                posStream.write((indent + line).toByteArray(cpCharset))
            }
            esc.feed(5).cut(EscPos.CutMode.FULL)
            esc.close()

        }
    }

}
