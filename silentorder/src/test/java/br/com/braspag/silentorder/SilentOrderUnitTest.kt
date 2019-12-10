package br.com.braspag.silentorder

import org.junit.Test
import org.junit.Before

class SilentOrderUnitTest {

    private lateinit var sdk: SilentOrderPost

    var validationErrors: List<ValidationResults> = mutableListOf()
    lateinit var error: ErrorResult

    @Before
    fun setup() {
        sdk = SilentOrderPost(Environment.SANDBOX)
    }

    @Test
    fun silentOrderPost_invalid() {

        // must change for each call
        sdk.accessToken = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"

        sdk.call(
            cardCvv = "xxx",
            onError = errorFunction,
            onSuccess = successFunction,
            onValidation = validationFunction
        )

        assert(validationErrors.isNotEmpty())
    }

    @Test
    fun silentOrderPost_isUnauthorized() {

        // must change for each call
        sdk.accessToken = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"

        sdk.call(
            cardHolderName = "Joselito Barbacena",
            cardNumber = "4000000000001091",
            cardExpirationDate = "10/2029",
            cardCvv = "621",
            onError = errorFunction,
            onSuccess = successFunction,
            onValidation = validationFunction
        )

        assert(error.errorCode.toInt() == 401)
    }

    private val validationFunction: ((v: List<ValidationResults>) -> Unit) = {
        println(">>>>>> Validation: $it")
        validationErrors = it
    }

    private val errorFunction: ((e: ErrorResult) -> Unit) = {
        println(">>>>>>>> Error - $it - errorCode: ${it.errorCode} - errorMessage: ${it.errorMessage}")
        error = it
    }

    private val successFunction: ((s: SuccessResult) -> Unit) = {
        println(">>>>>> Success - $it")
    }

}
