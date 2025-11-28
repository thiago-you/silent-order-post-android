package br.com.braspag.silentorder.data

import br.com.braspag.silentorder.model.CardFields
import br.com.braspag.silentorder.model.ValidationResults

class CardValidation {
    fun getValidationResult(cardField: CardFields) = ValidationResults(
        field = cardField.toString(),
        message = getValidationMessage(cardField)
    )
    
    private fun getValidationMessage(cardField: CardFields) = when (cardField) {
        CardFields.CARD_HOLDER_NAME -> "Nome do portador é um campo obrigatório!"
        CardFields.CARD_NUMBER -> "Número do cartão é um campo obrigatório!"
        CardFields.CARD_EXPIRATION -> "Data de expiração do cartão é um campo obrigatório!"
        CardFields.CARD_CVV -> "CVV é um campo obrigatório!"
        else -> ""
    }
}