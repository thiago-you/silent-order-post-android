package br.com.braspag.silentorder.data

import br.com.braspag.silentorder.model.Environment
import br.com.braspag.silentorder.model.ErrorResult
import br.com.braspag.silentorder.model.SilentOrderResult
import br.com.braspag.silentorder.model.SuccessResult

internal class RemoteDatasource(
    private val environment: Environment
) {
    fun silentOrder(
        accessToken: String,
        enableBinQuery: Boolean,
        cardHolderName: String,
        cardNumber: String,
        cardExpiration: String,
        cardCvv: String,
        onResult: SilentOrderResult
    ) {
        val formBody = ApiFormBuilder.getForm(
            accessToken = accessToken,
            enableBinQuery = enableBinQuery,
            cardHolderName = cardHolderName,
            cardNumber = cardNumber,
            cardExpiration = cardExpiration,
            cardCvv = cardCvv
        )

        val response = ApiClient(environment).sendRequest(formBody)

        if (response.isSuccessful) {
            val result = ApiResult().fromResponse(response)

            onResult.onSuccess(
                SuccessResult(
                    paymentToken = result?.paymentToken ?: "invalid",
                    brand = result?.brand,
                    foreignCard = result?.foreignCard,
                    binQueryReturnCode = result?.binQueryReturnCode,
                    binQueryReturnMessage = result?.binQueryReturnMessage
                )
            )
        } else {
            onResult.onError(
                ErrorResult(
                    errorCode = response.code.toString(),
                    errorMessage = response.message
                )
            )
        }
    }
}