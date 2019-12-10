package br.com.braspag.silentorder

import com.squareup.moshi.Json

data class SuccessResult(
    @Json(name = "PaymentToken") val paymentToken: String,
    @Json(name = "Brand") val brand: String?,
    @Json(name = "ForeignCard") val foreignCard: String?,
    @Json(name = "BinQueryReturnCode") val binQueryReturnCode: String?,
    @Json(name = "BinQueryReturnMessage") val binQueryReturnMessage: String?
)
