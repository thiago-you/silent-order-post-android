package br.com.braspag.silentorder.data

import br.com.braspag.silentorder.enums.Environment

class ApiEndpoints(
    private val environment: Environment
) {
    fun getUrl(): String =  when (environment) {
        Environment.PRODUCTION ->
            "https://transaction.cieloecommerce.cielo.com.br/post/api/public/v1/card"
        Environment.SANDBOX ->
            "https://transactionsandbox.pagador.com.br/post/api/public/v1/card"
    }
}
