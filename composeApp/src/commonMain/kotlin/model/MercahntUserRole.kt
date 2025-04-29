package model

import kotlinx.serialization.Serializable

@Serializable
enum class MerchantUserRole {
    ADMIN,
    CHEF,
    GARSON,
    DRIVER;
}