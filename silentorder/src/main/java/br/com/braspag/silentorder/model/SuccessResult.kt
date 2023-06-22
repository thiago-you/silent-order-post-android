package br.com.braspag.silentorder.model

import com.squareup.moshi.Json

data class SuccessResult(
    @Json(name = "PaymentToken") val paymentToken: String,
    @Json(name = "Brand") val brand: String?,
    @Json(name = "ForeignCard") val foreignCard: Boolean?,
    @Json(name = "BinQueryReturnCode") val binQueryReturnCode: String?,
    @Json(name = "BinQueryReturnMessage") val binQueryReturnMessage: String?
)
