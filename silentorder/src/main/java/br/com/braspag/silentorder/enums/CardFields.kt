package br.com.braspag.silentorder.enums

enum class CardFields {
    ACCESS_TOKEN,
    CARD_HOLDER_NAME,
    CARD_NUMBER,
    CARD_EXPIRATION,
    CARD_CVV,
    ENABLE_BIN_QUERY;

    override fun toString() = when(this) {
        ACCESS_TOKEN -> "AccessToken"
        CARD_HOLDER_NAME -> "HolderName"
        CARD_NUMBER -> "RawNumber"
        CARD_EXPIRATION -> "Expiration"
        CARD_CVV -> "SecurityCode"
        ENABLE_BIN_QUERY -> "EnableBinQuery"
    }
}