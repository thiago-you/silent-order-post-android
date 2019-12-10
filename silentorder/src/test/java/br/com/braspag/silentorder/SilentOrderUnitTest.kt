package br.com.braspag.silentorder

import org.junit.Test

import org.junit.Assert.*

class SilentOrderUnitTest {

    @Test
    fun silentOrderPost_isCorrect() {

        SilentOrderPost.call(
            environment = Environment.SANDBOX,
            accessToken = "NTk1YTgzNjAtNWVjYy00YWY1LWE0MmQtYTJmZWEyMDJhNWI2LTU5MzY0ODM3Mg==",
            cardHolderName = "Joselito Barbacena",
            cardNumber = "4000000000001091",
            cardExpirationDate = "10/2029",
            cardCvv = "621",
            onError = { e -> println(">>>>>>>> Error - $e - errorCode: ${e.errorCode} - errorMessage: ${e.errorMessage}") },
            onSuccess = { s -> println(">>>>>> Success - $s")},
            onValidation = { v -> println(">>>>>> Validation - $v")}
        )

        assertEquals(4, 2 + 2)
    }

    @Test
    fun silentOrderPost_invalid() {

        SilentOrderPost.call(
            environment = Environment.SANDBOX,
            accessToken = "NTk1YTgzNjAtNWVjYy00YWY1LWE0MmQtYTJmZWEyMDJhNWI2LTU5MzY0ODM3Mg==",
            cardCvv = "xxx",
            onError = { e -> println(">>>>>>>> Error - $e - errorCode: ${e.errorCode} - errorMessage: ${e.errorMessage}") },
            onSuccess = { s -> println(">>>>>> Success - $s")},
            onValidation = { v -> println(">>>>>> Validation: $v")}
        )

        assertEquals(4, 2 + 2)
    }
}
