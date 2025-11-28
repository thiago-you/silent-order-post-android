package br.com.braspag.silentorder.network

import br.com.braspag.silentorder.enums.Environment
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class ApiClient(
    private val environment: Environment,
) {
    fun sendRequest(formBody: FormBody): Response {
        val url = ApiEndpoints(environment).getUrl()

        val client = buildClient()
        
        val request = Request
            .Builder()
            .url(url)
            .post(formBody)
            .build()

        return client.newCall(request).execute()
    }

    private fun buildClient() = OkHttpClient
        .Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .addInterceptor(getLogInterceptor())
        .addInterceptor(RetryInterceptor.create())
        .build()

    private fun getLogInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
}
