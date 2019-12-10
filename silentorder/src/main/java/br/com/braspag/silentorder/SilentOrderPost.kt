package br.com.braspag.silentorder

import br.com.braspag.silentorder.data.RemoteDatasource
import br.com.braspag.silentorder.data.RemoteDatasource.Companion.FIELD_EXPIRATION
import br.com.braspag.silentorder.data.RemoteDatasource.Companion.FIELD_RAW_NUMBER
import br.com.braspag.silentorder.data.RemoteDatasource.Companion.FIELD_SECURITY_CODE
import br.com.braspag.silentorder.data.RemoteDatasource.Companion.FIELD_HOLDER

class SilentOrderPost(private val environment: Environment) {

    private var language = "PT"
    private var cvvvRequired = true
    private var mod10required = true
    private var enableBinQuery = false
    private var provider = "cielo"

    var accessToken: String = ""

    fun call(
        cardHolderName: String = "",
        cardNumber: String = "",
        cardExpirationDate: String = "",
        cardCvv: String = "",
        enableBinquery: Boolean? = false,
        onValidation: ((List<ValidationResults>) -> Unit)?,
        onSuccess: ((SuccessResult) -> Unit)?,
        onError: ((ErrorResult) -> Unit)?
    ) {

        // validate input
        val validationErrors = validate(cardHolderName, cardNumber, cardExpirationDate, cardCvv)
        if (validationErrors.isNotEmpty()) {
            onValidation?.invoke(validationErrors)
            return
        }

        // call API
        RemoteDatasource().silentOrder(
            environment,
            accessToken,
            cardHolderName,
            cardNumber,
            cardExpirationDate,
            cardCvv,
            onValidation,
            onSuccess,
            onError
        )

        // return the results
    }

    private fun validate(
        cardHolderName: String = "",
        cardNumber: String = "",
        cardExpirationDate: String = "",
        cardCvv: String = ""
    ): List<ValidationResults> {

        // validate input
        val validationErrors = mutableListOf<ValidationResults>()

        if (cardHolderName.isEmpty()) {
            validationErrors.add(
                ValidationResults(
                    FIELD_HOLDER,
                    "Nome do portador é um campo obrigatório!"
                )
            )
        }

        if (cardNumber.isEmpty()) {
            validationErrors.add(
                ValidationResults(
                    FIELD_RAW_NUMBER,
                    "Número do cartão é um campo obrigatório!"
                )
            )
        }

        if (cardExpirationDate.isEmpty()) {
            validationErrors.add(
                ValidationResults(
                    FIELD_EXPIRATION,
                    "Data de expiração do cartão é um campo obrigatório!"
                )
            )
        }

        if (cardCvv.isEmpty()) {
            validationErrors.add(
                ValidationResults(
                    FIELD_SECURITY_CODE,
                    "CVV é um campo obrigatório!"
                )
            )
        }

        return validationErrors
    }
}


