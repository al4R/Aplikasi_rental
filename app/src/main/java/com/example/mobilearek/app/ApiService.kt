package com.example.mobilearek.app



import com.example.mobilearek.model.ResponModel
import com.example.mobilearek.model.Pesanan
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @Multipart
    @POST("register")
    fun register(
        @Part("name")  nama:RequestBody,
        @Part("email") email :RequestBody,
        @Part("password") password :RequestBody,
        @Part("telepon") telepon :RequestBody,
        @Part("nik") nik :RequestBody,
        @Part ktp: MultipartBody.Part? = null
    ): Call<ResponModel>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email :String,
        @Field("password") password :String
    ): Call<ResponModel>

    @Multipart
    @POST("uimage/{id}")
    fun ubahPp(
        @Path("id") id: Int,
        @Part image: MultipartBody.Part? = null
    ): Call<ResponModel>

    @POST("pesan")
    fun pesan(
        @Body data :Pesanan
    ): Call<ResponModel>

    @FormUrlEncoded
    @POST("upuser/{id}")
    fun updateUser(
        @Path("id") id: Int,
        @Field("email") email :String,
        @Field("name") nama :String,
        @Field("telepon") telepon :String
    ): Call<ResponModel>

    @GET("mobil")
    fun getMobil(): Call<ResponModel>


    @POST("search/{q}")
    fun searchMobil(
        @Path("q") q:String
    ): Call<ResponModel>

    @GET("mobilpage")
    fun getmobilpage(
        @QueryMap param : HashMap <String, String>
    ):Call<ResponModel>

    @GET("history/{id}")
        fun getRiwayat(
            @Path("id") id: Int
        ): Call<ResponModel>

    @GET("berjalan/{id}")
    fun getTransaksi(
        @Path("id") id: Int
    ): Call<ResponModel>

    @Multipart
    @POST("upload/{id}")
    fun upload(
        @Path("id") id: Int,
        @Part bukti_tf: MultipartBody.Part? = null
    ): Call<ResponModel>

    @FormUrlEncoded
    @POST("updatemobil/{id}")
    fun updatemobil(
        @Path("id") id: Int,
        @Field("status") status: Int
    ): Call<ResponModel>

    @FormUrlEncoded
    @POST("cancel/{id}")
    fun cancel(
        @Path("id") id: Int,
        @Field("cancel") status: Int
    ): Call<ResponModel>

    @FormUrlEncoded
    @POST("upass/{id}")
    fun editPass(
        @Path("id") id: Int,
        @Field("password") password: String,
        @Field("new_password") new_password: String
    ): Call<ResponModel>

    @FormUrlEncoded
    @POST("lupapass")
    fun lupapass(
        @Field("email") email :String,
        @Field("nik") nik :String
    ): Call<ResponModel>

    @FormUrlEncoded
    @POST("resetpass/{id}")
    fun resetpass(
        @Path("id") id: Int,
        @Field("password") password: String
    ): Call<ResponModel>


}