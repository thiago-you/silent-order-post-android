package br.com.braspag.silentorder.model

import com.google.gson.annotations.SerializedName

data class SuccessResult(
    @SerializedName("PaymentToken")
    val paymentToken: String?,
    @SerializedName("Brand")
    val brand: String?,
    @SerializedName("ForeignCard")
    val foreignCard: Boolean?,
    @SerializedName("BinQueryReturnCode")
    val binQueryReturnCode: String?,
    @SerializedName("BinQueryReturnMessage")
    val binQueryReturnMessage: String?
)
