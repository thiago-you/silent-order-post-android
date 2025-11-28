package br.com.braspag.silentorder.model

data class ApiResult(
    val code : String,
    val message : String,
    val result: SuccessResult?
)