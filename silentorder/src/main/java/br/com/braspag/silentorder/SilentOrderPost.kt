package br.com.braspag.silentorder

import br.com.braspag.silentorder.data.CardValidation
import br.com.braspag.silentorder.data.RemoteDatasource
import br.com.braspag.silentorder.model.CardFields
import br.com.braspag.silentorder.model.Environment
import br.com.braspag.silentorder.model.SilentOrderResult
import br.com.braspag.silentorder.model.ValidationResults

class SilentOrderPost(
    private val environment: Environment
) {
    var accessToken: String = ""
    var enableBinQuery = false

    fun sendCardData(
        cardHolderName: String = "",
        cardNumber: String = "",
        cardExpirationDate: String = "",
        cardCvv: String = "",
        onResult: SilentOrderResult
    ) {
        val validationErrors = validate(cardHolderName, cardNumber, cardExpirationDate, cardCvv)

        if (validationErrors.isNotEmpty()) {
            return onResult.onValidation(validationErrors)
        }

        RemoteDatasource(environment).silentOrder(
            accessToken = accessToken,
            enableBinQuery = enableBinQuery,
            cardHolderName = cardHolderName,
            cardNumber = cardNumber,
            cardExpiration = cardExpirationDate,
            cardCvv = cardCvv,
            onResult = onResult
        )
    }

    private fun validate(
        cardHolderName: String,
        cardNumber: String,
        cardExpirationDate: String,
        cardCvv: String
    ): List<ValidationResults> {
        val validationErrors = mutableListOf<ValidationResults>()

        if (cardHolderName.isEmpty()) {
            validationErrors.add(CardValidation().getValidationResult(CardFields.CARD_HOLDER_NAME))
        }
        if (cardNumber.isEmpty()) {
            validationErrors.add(CardValidation().getValidationResult(CardFields.CARD_NUMBER))
        }
        if (cardExpirationDate.isEmpty()) {
            validationErrors.add(CardValidation().getValidationResult(CardFields.CARD_EXPIRATION))
        }
        if (cardCvv.isEmpty()) {
            validationErrors.add(CardValidation().getValidationResult(CardFields.CARD_CVV))
        }

        return validationErrors
    }
}
