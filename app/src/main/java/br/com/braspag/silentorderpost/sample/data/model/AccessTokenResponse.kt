package br.com.braspag.silentorderpost.sample.data.model

import com.squareup.moshi.Json

data class AccessTokenResponse(
    @Json(name = "MerchantId") val merchantId: String,
    @Json(name = "AccessToken") val accessToken: String,
    @Json(name = "Issued") val issued: String,
    @Json(name = "ExpiresIn") val expiresIn: String
)