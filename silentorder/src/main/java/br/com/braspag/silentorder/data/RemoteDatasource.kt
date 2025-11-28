package br.com.braspag.silentorder.data

import br.com.braspag.silentorder.enums.Environment
import br.com.braspag.silentorder.model.ApiResult
import br.com.braspag.silentorder.model.SilentOrderResult

internal class RemoteDatasource(
    private val environment: Environment,
) {
    fun silentOrder(
        accessToken: String,
        enableBinQuery: Boolean,
        cardHolderName: String,
        cardNumber: String,
        cardExpiration: String,
        cardCvv: String,
        onResult: SilentOrderResult,
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

        val successResult = ApiResultHandler().fromResponse(response)

        val apiResult = ApiResult(
            code = response.code.toString(),
            message = response.message,
            data = successResult
        )
        
        onResult.onResult(apiResult)
    }
}
