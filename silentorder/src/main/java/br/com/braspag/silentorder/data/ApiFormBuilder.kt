package br.com.braspag.silentorder.data

import br.com.braspag.silentorder.enums.CardFields
import okhttp3.FormBody

object ApiFormBuilder {
    fun getForm(
        accessToken: String,
        enableBinQuery: Boolean,
        cardHolderName: String,
        cardNumber: String,
        cardExpiration: String,
        cardCvv: String
    ): FormBody = FormBody.Builder()
        .add(CardFields.ACCESS_TOKEN.toString(), accessToken)
        .add(CardFields.CARD_HOLDER_NAME.toString(), cardHolderName)
        .add(CardFields.CARD_NUMBER.toString(), cardNumber)
        .add(CardFields.CARD_EXPIRATION.toString(), cardExpiration)
        .add(CardFields.CARD_CVV.toString(), cardCvv)
        .add(CardFields.ENABLE_BIN_QUERY.toString(), enableBinQuery.toString())
        .build()
}
