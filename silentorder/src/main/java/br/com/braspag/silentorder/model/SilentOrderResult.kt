package br.com.braspag.silentorder.model

interface SilentOrderResult {
    fun onValidation(errors: List<ValidationResults>)
    fun onSuccess(result: SuccessResult)
    fun onError(result: ErrorResult)
}