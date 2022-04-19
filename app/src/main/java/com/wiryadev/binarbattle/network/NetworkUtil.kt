package com.wiryadev.binarbattle.network

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

object NetworkUtil {

    fun createRequestBody(vararg params: Pair<String, Any>) =
        JSONObject(mapOf(*params)).toString()
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

}