package br.com.braspag.silentorder.model

interface SilentOrderResult {
    fun onValidation(errors: List<ValidationResults>)
    fun onResult(result: ApiResult)
}