package org.example.common.httpClient

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.example.common.exceptions.CustomException
import org.example.common.exceptions.Errcode

import org.springframework.stereotype.Component

@Component
class CallClient (
    private val httpClient: OkHttpClient
){
    fun GET(uri:String, headers: Map<String,String> = emptyMap()): String {
        val requestBuilder = Request.Builder().url(uri)
        headers.forEach { (key,value) -> requestBuilder.addHeader(key,value) }
        val request = requestBuilder.build()

        return resultHandler(httpClient.newCall(request).execute())
    }

    fun POST(uri:String, headers: Map<String,String> = emptyMap(), body: RequestBody): String {
        val requestBuilder = Request.Builder().url(uri).post(body)
        headers.forEach { (key,value) -> requestBuilder.addHeader(key,value) }
        val request = requestBuilder.build()

        return resultHandler(httpClient.newCall(request).execute())
    }

    private fun resultHandler(response: Response): String {
        response.use {
            if(!it.isSuccessful){
                var msg = "Http ${it.code}: ${it.body?.string() ?: "unknown error"} "
                throw CustomException(Errcode.FAILED_TO_CALL_CLINET, msg)
            }

            return it.body?.string() ?: throw CustomException(Errcode.AUTH_CONFIG_NOT_FOUND)
        }
    }
}