package com.example.mobilearek.app


import com.example.mobilearek.model.ResponModel
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") nama :String,
        @Field("email") email :String,
        @Field("password") password :String,
        @Field("telepon") telepon :String,
        @Field("nik") nik :String
    ): Call<ResponModel>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email :String,
        @Field("password") password :String
    ): Call<ResponModel>

    @FormUrlEncoded
    @PATCH("update")
    fun update(): Call<ResponModel>

    @GET("mobil")
    fun getMobil(): Call<ResponModel>
}