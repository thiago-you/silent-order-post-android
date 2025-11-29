package br.com.braspag.silentorderpost.sample.data.remote

import br.com.braspag.silentorderpost.sample.data.model.AccessTokenResponse
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.RequestBody.Companion.toRequestBody

object TokenRemoteDatasource {

    private const val SANDBOX_URL =
        "https://transactionsandbox.pagador.com.br/post/api/public/v1/accesstoken?merchantid="

    fun getAccessToken(
        merchantId: String,
        onSuccess: ((String) -> Unit),
        onError: ((String) -> Unit)
    ) {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val request = Request.Builder()
            .url(SANDBOX_URL.plus(merchantId))
            .post("".toRequestBody())
            .build()

        val response = client.newCall(request).execute()
        
        if (response.isSuccessful) {
            val result = Gson().fromJson(
                response.body?.string() ?: "",
                AccessTokenResponse::class.java
            )
            
            onSuccess.invoke(result?.accessToken ?: "")
        } else {
            onError.invoke("Error ${response.code}")
        }
    }
}