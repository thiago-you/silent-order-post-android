package br.com.braspag.silentorder.network

import android.util.Log
import br.com.braspag.silentorder.model.SuccessResult
import com.google.gson.Gson
import okhttp3.Response

class ApiResultHandler(
    private val logResponse: Boolean
) {
    fun fromResponse(response: Response): SuccessResult? {
        if (!response.isSuccessful) {
            return null
        }

        val data = response.body?.string().orEmpty()

        if (logResponse) {
            Log.e("SilentOrderPostResult", data)
        }

        return Gson().fromJson(data, SuccessResult::class.java)
    }
}