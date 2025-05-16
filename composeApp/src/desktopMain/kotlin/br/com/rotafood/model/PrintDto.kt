package model

import kotlinx.serialization.Serializable

@Serializable
data class PrintDto(
    val printerName: String,
    val text: String,
    val marginPt: Int,
    val widthMm: Int,
    val useStyle: Boolean
)