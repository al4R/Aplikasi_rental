package com.example.mobilearek.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import com.example.mobilearek.R
import com.example.mobilearek.app.ApiConfig
import kotlinx.android.synthetic.main.activity_daftar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.Toast
import com.example.mobilearek.MainActivity
import com.example.mobilearek.helper.SharedPref
import com.example.mobilearek.model.ResponModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_daftar.btn_daftar
import kotlinx.android.synthetic.main.activity_daftar.edit_email
import kotlinx.android.synthetic.main.activity_daftar.progress_bar
import kotlinx.android.synthetic.main.activity_profil_image.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File


class DaftarActivity : AppCompatActivity() {

    lateinit var s:SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar)
        s= SharedPref(this)
        mainbutton()


    }
    fun mainbutton(){
        btn_daftar.setOnClickListener {
            daftar()
        }
        btn_cls.setOnClickListener {
            val intent = Intent (this@DaftarActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
        btn_upload_ktp.setOnClickListener {
            EasyImage.openGallery(this,1)
        }
    }

    fun daftar() {
        if (edit_nama.text.toString().isEmpty()) {
            edit_nama.error = "Kolom Nama tidak boleh kosong"
            edit_nama.requestFocus()
            return
        } else if(edit_email.text.toString().isEmpty()) {
            edit_email.error = "Email tidak boleh kosong"
            edit_email.requestFocus()
            return
        }else if(edit_telepon.text.toString().isEmpty() ) {
            edit_telepon.error = "Telepon tidak boleh kosong"
            edit_telepon.requestFocus()
            return
        }else if(edit_telepon.length() < 9 && edit_telepon.length() > 13 ){
            edit_telepon.error = "Nomor Telepon tidak valid"
            edit_telepon.requestFocus()
            return
        }else if(edit_password.text.toString().isEmpty()) {
            edit_password.error = "Password tidak boleh kosong"
            edit_password.requestFocus()
            return
        }else if(edit_password.length() < 7){
            edit_password.error = "Minimal 8 karakter"
            edit_password.requestFocus()
            return
        }else if(edit_password.text.toString() != edit_konfir.text.toString() ){
            edit_password.error = "Tidak cocok"
            edit_konfir.error = "Tidak cocok"
            edit_konfir.requestFocus()
            return
        }else if(edit_nik.text.toString().isEmpty() ) {
            edit_nik.error = "NIK tidak boleh kosong"
            edit_nik.requestFocus()
            return
        }else if(edit_nik.length() < 16 && edit_nik.length() > 16  ){
            edit_nik.error = "NIK tidak valid"
            edit_nik.requestFocus()
            return
        }else if(fileImg == null){
            Toast.makeText(this, "Foto ktp tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }else if(edit_email.text.toString().isNotEmpty()) {
            if (Patterns.EMAIL_ADDRESS.matcher(edit_email.text.toString()).matches() == false) {
                edit_email.error = "Email tidak valid"
                edit_email.requestFocus()
                return
            }
        }
        val img = convertFile(fileImg!!)
        val nama = convertString(edit_nama.text.toString())
        val email = convertString(edit_email.text.toString())
        val password = convertString(edit_password.text.toString())
        val telepon = convertString(edit_telepon.text.toString())
        val nik = convertString(edit_nik.text.toString())
        progress_bar.visibility = View.VISIBLE
        ApiConfig.instanceRetrofit.register(
           nama, email, password, telepon, nik, img
        ).enqueue(object : Callback<ResponModel> {
            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                progress_bar.visibility = View.GONE
                val respon = response.body()
                if (respon != null) {
                    if (respon.success == 1){
                        val intent = Intent (this@DaftarActivity, LoginActivity::class.java)
                        Toast.makeText(this@DaftarActivity, "Berhasil daftar", Toast.LENGTH_SHORT).show()
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this@DaftarActivity, "Error:" + respon.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                progress_bar.visibility = View.GONE
                Toast.makeText(this@DaftarActivity, "Error:" + t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
    var fileImg: File? =null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        EasyImage.handleActivityResult(requestCode,resultCode,data,this,object :DefaultCallback(){
            override fun onImagePicked(
                imageFile: File?,
                source: EasyImage.ImageSource?,
                type: Int
            ) {
                fileImg = imageFile
                if (imageFile != null) {
                    Picasso.get()
                        .load(imageFile)
                        .resize(540,300)
                        .into(iv_ktp)
                }
            }

        })
    }
    fun convertFile(file: File): MultipartBody.Part{
        val reqFile: RequestBody = RequestBody.create(MediaType.parse("ktp/*"),file)
        return MultipartBody.Part.createFormData("ktp",file.name,reqFile)

    }
    fun convertString(string: String):RequestBody{
        return RequestBody.create(MediaType.parse("text/plain"),string)
    }
}