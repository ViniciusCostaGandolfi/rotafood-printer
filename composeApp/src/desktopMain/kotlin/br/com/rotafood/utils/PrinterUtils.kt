import com.github.anastaciocintra.escpos.EscPos
import com.github.anastaciocintra.escpos.Style
import com.github.anastaciocintra.output.PrinterOutputStream
import java.nio.charset.Charset
import javax.print.PrintServiceLookup
import kotlin.math.roundToInt

fun printText(
    printerName: String?,
    text: String,
    widthMm: Int,
    marginPt: Int,
    useStyle: Boolean
) {
    val service = PrintServiceLookup.lookupPrintServices(null, null)
        .firstOrNull { it.name.equals(printerName, ignoreCase = true) }
        ?: throw IllegalArgumentException("Impressora '$printerName' não encontrada")

    val cols = if (widthMm >= 70) 48 else 32
    val marginCols = ((marginPt.toFloat() / widthMm.toFloat()) * cols)
        .roundToInt()
        .coerceAtMost(cols / 4)
    val indent = " ".repeat(marginCols)

    PrinterOutputStream(service).use { posStream ->
        EscPos(posStream).use { esc ->
            esc.initializePrinter()
            esc.setPrinterCharacterTable(EscPos.CharacterCodeTable.CP860_Portuguese.value)
            esc.setCharsetName("CP860")

            if (useStyle) {
                val style = Style()
                    .setFontSize(Style.FontSize._1, Style.FontSize._1)
                esc.setStyle(style)
            }

            text.lines().forEach { line ->
                val content = indent + line + "\n"
                if (useStyle) {
                    esc.writeLF(content)
                } else {
                    try {
                        val cpCharset = Charset.forName("CP860")
                        posStream.write(content.toByteArray(cpCharset))
                    } catch (err: Exception) {
                        posStream.write(content.toByteArray(Charsets.UTF_8))
                    }
                }
            }

            esc.feed(2)
            esc.cut(EscPos.CutMode.PART)
            esc.feed(2)
            esc.flush()
        }
        posStream.flush()
    }
}
