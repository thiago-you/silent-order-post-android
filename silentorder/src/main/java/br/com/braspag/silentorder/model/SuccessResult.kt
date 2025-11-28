package br.com.braspag.silentorder.model

import com.squareup.moshi.Json

data class SuccessResult(
    @field:Json(name = "PaymentToken") val paymentToken: String,
    @field:Json(name = "Brand") val brand: String?,
    @field:Json(name = "ForeignCard") val foreignCard: Boolean?,
    @field:Json(name = "BinQueryReturnCode") val binQueryReturnCode: String?,
    @field:Json(name = "BinQueryReturnMessage") val binQueryReturnMessage: String?
)
