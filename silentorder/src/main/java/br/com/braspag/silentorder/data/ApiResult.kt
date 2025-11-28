package br.com.braspag.silentorder.data

import br.com.braspag.silentorder.model.SuccessResult
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Response

class ApiResult {
    fun fromResponse(response: Response): SuccessResult? {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val successJsonAdapter = moshi.adapter(SuccessResult::class.java)

        return successJsonAdapter.fromJson(response.body?.string().orEmpty())
    }
}