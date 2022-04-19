package com.wiryadev.binarbattle.network

import com.wiryadev.binarbattle.entity.CommonResponse
import com.wiryadev.binarbattle.entity.LoginResponse
import com.wiryadev.binarbattle.entity.UpdateResponse
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @POST("auth/register")
    fun register(@Body requestBody: RequestBody): Observable<CommonResponse>

    @POST("auth/login")
    fun login(@Body requestBody: RequestBody): Observable<LoginResponse>

    @GET("auth/me")
    fun auth(@Header("Authorization") token: String): Observable<CommonResponse>

    @Multipart
    @PUT("users")
    fun update(
        @Header("Authorization") token: String,
        @Part("username") username: RequestBody,
        @Part("email") email: RequestBody,
        @Part file: MultipartBody.Part,
    ): Observable<UpdateResponse>

}