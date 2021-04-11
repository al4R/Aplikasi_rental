package com.example.mobilearek.app


import com.example.mobilearek.model.ResponModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

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
}