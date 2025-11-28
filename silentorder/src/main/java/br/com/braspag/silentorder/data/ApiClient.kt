package br.com.braspag.silentorder.data

import br.com.braspag.silentorder.model.Environment
import br.com.braspag.silentorder.model.SuccessResult
import br.com.braspag.silentorderpost.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor

class ApiClient(
    private val environment: Environment,
) {
    fun sendRequest(formBody: FormBody): Response {
        val url = ApiEndpoints(environment).getUrl()

        val xSdkVersion = BuildConfig.X_SDK_VERSION

        val client = buildClient()
        
        val request = Request
            .Builder()
            .addHeader("x-sdk-version", xSdkVersion)
            .url(url)
            .post(formBody)
            .build()

        return client.newCall(request).execute()
    }

    private fun buildClient() = OkHttpClient
        .Builder()
        .addInterceptor(getLogInterceptor())
        .build()

    private fun getLogInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
}
