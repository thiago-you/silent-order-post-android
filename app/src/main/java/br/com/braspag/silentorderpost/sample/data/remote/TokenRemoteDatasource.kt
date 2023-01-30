package br.com.braspag.silentorderpost.sample.data.remote

import br.com.braspag.silentorderpost.sample.data.model.AccessTokenResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
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
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val successJsonAdapter = moshi.adapter(AccessTokenResponse::class.java)

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
            val result = successJsonAdapter.fromJson(response.body?.string() ?: "")
            onSuccess.invoke(result?.accessToken ?: "")
        } else {
            onError.invoke("Error ${response.code}")
        }
    }
}