import java.io.ByteArrayOutputStream
import javax.print.DocFlavor
import javax.print.PrintServiceLookup
import javax.print.SimpleDoc
import kotlin.math.roundToInt

fun printText(
    printerName: String?,
    text: String,
    widthMmStr: String,
    fontSizePtStr: String,
    marginMmStr: String,
    lineSpacingPtStr: String,
    linesToFeed: Int = 4
) {
    val service = PrintServiceLookup.lookupPrintServices(null, null)
        .firstOrNull { it.name.equals(printerName, ignoreCase = true) }
        ?: error("Impressora '$printerName' não encontrada")

    val widthMm       = widthMmStr.toFloatOrNull()     ?: 58f
    val fontSizePt    = fontSizePtStr.toFloatOrNull()  ?: 10f
    val marginMm      = marginMmStr.toFloatOrNull()    ?: 0f
    val lineSpacingPt = lineSpacingPtStr.toIntOrNull() ?: 30


    val cols         = if (widthMm >= 70f) 48 else 32
    val marginCols   = ((marginMm / widthMm) * cols).roundToInt().coerceAtMost(cols/4)
    val indent       = " ".repeat(marginCols)

    val multiplier = when {
        fontSizePt <= 11f -> 1
        fontSizePt <= 17f -> 2
        fontSizePt <= 23f -> 3
        fontSizePt <= 29f -> 4
        fontSizePt <= 35f -> 5
        fontSizePt <= 41f -> 6
        fontSizePt <= 47f -> 7
        else              -> 8
    }
    val sizeByte = ((multiplier - 1) shl 4) or (multiplier - 1)

    val lineSpacingDots = (lineSpacingPt * (203f/72f)).roundToInt().coerceIn(0,255)

    val baos = ByteArrayOutputStream().apply {
        write(0x1B); write(0x40)
        write(0x1D); write(0x21); write(sizeByte)
        write(0x1B); write(0x33); write(lineSpacingDots)

        text.lines().forEach { line ->
            write((indent + line).toByteArray(Charsets.ISO_8859_1))
            write(0x0A)
        }

        write(0x1B); write(0x32)
        write(0x1B); write(0x64); write(linesToFeed)
        write(0x1D); write(0x56); write(0x00)
    }

    val doc = SimpleDoc(baos.toByteArray(), DocFlavor.BYTE_ARRAY.AUTOSENSE, null)
    service.createPrintJob().print(doc, null)
}
