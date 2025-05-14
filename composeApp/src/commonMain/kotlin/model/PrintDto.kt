package model

import kotlinx.serialization.Serializable

@Serializable
data class PrintDto(
    val printerName: String,
    val text: String,
    val columns: Int,
    val fontSizePt: Int,
    val widthMm: Float
)