package model

import kotlinx.serialization.Serializable

@Serializable
data class MerchantUserDto(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val role: String,
    val merchantId: String,
)
