package br.com.braspag.silentorder.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import kotlin.math.min
import kotlin.math.pow
import kotlin.random.Random

class RetryInterceptor private constructor(
    private val maxRetries: Int = 3,
    private val initialDelay: Long = 1000L,
    private val multiplier: Double = 2.0,
    private val maxDelay: Long = 3000L,
    private val retryOnServerError: Boolean = true,
    private val retryOnClientErrors: Set<Int> = setOf(408, 429)
) : Interceptor {

    companion object RetryInterceptorFactory {
        fun create() = RetryInterceptor()
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        var response: Response? = null
        var exception: IOException? = null
        var attempts = 0

        while (attempts < maxRetries) {
            try {
                response?.body?.close()

                response = chain.proceed(request)

                if (isSuccessful(response) || !shouldRetryResponseCode(response)) {
                    return response
                }

                response.body?.close()

                val delay = calculateDelay(attempts)

                TimeUnit.MILLISECONDS.sleep(delay)
            } catch (e: IOException) {
                exception = e

                if (!shouldRetryException(e) || attempts >= maxRetries - 1) {
                    throw exception
                }

                val delay = calculateDelay(attempts)

                TimeUnit.MILLISECONDS.sleep(delay)
            }

            attempts++
        }

        throw exception ?: IOException("Unexpected error occurred after $maxRetries attempts")
    }

    private fun isSuccessful(response: Response): Boolean = response.isSuccessful

    private fun shouldRetryResponseCode(response: Response): Boolean {
        val code = response.code

        return when (code) {
            in 500..599 -> retryOnServerError
            in retryOnClientErrors -> true
            else -> false
        }
    }

    private fun shouldRetryException(exception: IOException): Boolean = when (exception) {
        is SocketTimeoutException -> true
        is UnknownHostException -> true

        else -> {
            val message = exception.message?.lowercase() ?: ""

            message.contains("timeout") ||
                message.contains("connection") ||
                message.contains("refused") ||
                message.contains("reset")
        }
    }

    private fun calculateDelay(attempt: Int): Long {
        val baseDelay = initialDelay * multiplier.pow(attempt.toDouble())

        val cappedDelay = min(baseDelay.toLong(), maxDelay)

        val jitter = (Random.nextDouble() * 0.3 * cappedDelay).toLong()

        return cappedDelay + jitter
    }
}
