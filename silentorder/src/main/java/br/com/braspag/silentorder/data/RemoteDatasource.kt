package br.com.braspag.silentorder.data

import br.com.braspag.silentorder.Environment
import br.com.braspag.silentorder.ErrorResult
import br.com.braspag.silentorder.SuccessResult
import br.com.braspag.silentorder.ValidationResults
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor

internal class RemoteDatasource {

    companion object {
        const val SANDBOX_URL = "https://transactionsandbox.pagador.com.br/post/api/public/v1/card"
        const val PRODUCTION_URL =
            "https://transaction.cieloecommerce.cielo.com.br/post/api/public/v1/card"

        const val FIELD_ACCESS_TOKEN = "AccessToken"
        const val FIELD_HOLDER = "HolderName"
        const val FIELD_RAW_NUMBER = "RawNumber"
        const val FIELD_EXPIRATION = "Expiration"
        const val FIELD_SECURITY_CODE = "SecurityCode"
        const val FIELD_ENABLE_BINQUERY = "EnableBinQuery"
    }

    fun silentOrder(
        environment: Environment,
        accessToken: String,
        cardHolderName: String,
        cardNumber: String,
        cardExpiration: String,
        cardSecurityCode: String,
        enableBinQuery: Boolean,
        onValidation: ((List<ValidationResults>) -> Unit)?,
        onSuccess: ((SuccessResult) -> Unit)?,
        onError: ((ErrorResult) -> Unit)?
    ) {

        val url = if (environment == Environment.PRODUCTION) PRODUCTION_URL else SANDBOX_URL

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val successJsonAdapter = moshi.adapter<SuccessResult>(SuccessResult::class.java)

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val formBody = FormBody.Builder()
            .add(FIELD_ACCESS_TOKEN, accessToken)
            .add(FIELD_HOLDER, cardHolderName)
            .add(FIELD_RAW_NUMBER, cardNumber)
            .add(FIELD_EXPIRATION, cardExpiration)
            .add(FIELD_SECURITY_CODE, cardSecurityCode)
            .add(FIELD_ENABLE_BINQUERY, enableBinQuery.toString())
            .build()

        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()

        val response = client.newCall(request).execute()

        if (response.isSuccessful) {
            val result = successJsonAdapter.fromJson(response.body?.string() ?: "")

            onSuccess?.invoke(
                SuccessResult(
                    paymentToken = result?.paymentToken ?: "invalid",
                    brand = result?.brand,
                    foreignCard = result?.foreignCard,
                    binQueryReturnCode = result?.binQueryReturnCode,
                    binQueryReturnMessage = result?.binQueryReturnMessage
                )
            )
        } else {

            val body = response.body

            onError?.invoke(
                ErrorResult(
                    errorCode = response.code.toString(),
                    errorMessage = response.message
                )
            )
        }
    }
}