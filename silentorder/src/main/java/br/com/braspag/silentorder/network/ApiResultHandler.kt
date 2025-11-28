package br.com.braspag.silentorder.network

import android.util.Log
import br.com.braspag.silentorder.model.SuccessResult
import com.google.gson.Gson
import okhttp3.Response

class ApiResultHandler {
    fun fromResponse(response: Response): SuccessResult? {
        if (!response.isSuccessful) {
            return null
        }

        val data = response.body?.string().orEmpty()

        Log.e("TESTE", data)

        return Gson().fromJson(data, SuccessResult::class.java)
    }
}