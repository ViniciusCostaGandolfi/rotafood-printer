package utils

import java.io.ByteArrayInputStream
import javax.print.DocFlavor
import javax.print.PrintService
import javax.print.SimpleDoc
import javax.print.attribute.HashPrintRequestAttributeSet
import javax.print.attribute.standard.MediaPrintableArea


fun PrintService.printPdf(bytes: ByteArray, larguraMm: Int) {
    val job = createPrintJob()

    val doc = SimpleDoc(
        ByteArrayInputStream(bytes),
        DocFlavor.INPUT_STREAM.AUTOSENSE,
        null
    )

    val attrs = HashPrintRequestAttributeSet().apply {
        add(MediaPrintableArea(0f, 0f, larguraMm.toFloat(), Float.POSITIVE_INFINITY, MediaPrintableArea.MM))
    }

    job.print(doc, attrs)
}
