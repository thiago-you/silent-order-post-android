package br.com.braspag.silentorder.data

import br.com.braspag.silentorder.enums.Environment
import br.com.braspag.silentorder.model.ApiResult
import br.com.braspag.silentorder.model.SilentOrderResult
import br.com.braspag.silentorder.network.ApiClient
import br.com.braspag.silentorder.network.ApiFormBuilder
import br.com.braspag.silentorder.network.ApiResultHandler

internal class RemoteDatasource(
    private val environment: Environment,
) {
    fun silentOrder(
        accessToken: String,
        enableBinQuery: Boolean,
        logResponse: Boolean,
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

        val successResult = ApiResultHandler(logResponse).fromResponse(response)

        val apiResult = ApiResult(
            code = response.code.toString(),
            message = response.message,
            data = successResult
        )
        
        onResult.onResult(apiResult)
    }
}
