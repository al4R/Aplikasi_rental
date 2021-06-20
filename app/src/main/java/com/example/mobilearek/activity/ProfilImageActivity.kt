package com.example.mobilearek.activity


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobilearek.MainActivity
import com.example.mobilearek.R
import com.example.mobilearek.app.ApiConfig
import com.example.mobilearek.helper.SharedPref
import com.example.mobilearek.model.ResponModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profil_image.*
import kotlinx.android.synthetic.main.toolbar.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

@Suppress("DEPRECATION")
class ProfilImageActivity : AppCompatActivity() {

    lateinit var s:SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil_image)
        s= SharedPref(this)


        mainbutton()
    }
    var fileImg: File? =null
    fun mainbutton(){
        iv_profil.setOnClickListener {
            EasyImage.openGallery(this,1)
        }
        btn_btl.setOnClickListener {
            onBackPressed()
        }

        btn_smpn.setOnClickListener {
            if(fileImg == null){
                toast("Pilih gambar")
            }else {
                progress_bar.visibility = View.VISIBLE
                uploadImage()
            }
        }
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Ubah foto"
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
    private fun uploadImage() {
        val id = s.getUser()!!.id
        val img = convertFile(fileImg!!)
        ApiConfig.instanceRetrofit.ubahPp(id,img).enqueue(object : Callback<ResponModel> {
            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val respon = response.body()
                if(respon != null){
                    if (respon.success == 1){
                        progress_bar.visibility = View.GONE
                        s.setUser(respon.user)
                        s.setString(s.image,respon.user.image)
                        Toast.makeText(this@ProfilImageActivity, respon.user.image, Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    }else{
                        Toast.makeText(this@ProfilImageActivity, "Error : " + respon.message, Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this@ProfilImageActivity, "Tidak ada respon" , Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                progress_bar.visibility = View.GONE
                Toast.makeText(this@ProfilImageActivity, "Error:" + t.message, Toast.LENGTH_SHORT).show()
            }

        })

    }
    fun toast(s: String){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        EasyImage.handleActivityResult(requestCode,resultCode,data,this, object : DefaultCallback(){
            override fun onImagePicked(
                imageFile: File?, source: EasyImage.ImageSource?, type: Int) {
                fileImg = imageFile
                if (imageFile != null) {
                    Picasso.get()
                        .load(imageFile)
                        .placeholder(R.drawable.ic_baseline_arrow_back_24)
                        .error(R.drawable.ic_baseline_close_24)
                        .resize(400,400)
                        .into(iv_profil)
                }
            }

        })
    }

    fun convertFile(file: File): MultipartBody.Part{
        val reqFile: RequestBody = RequestBody.create(MediaType.parse("image/*"),file)
        return MultipartBody.Part.createFormData("image",file.name,reqFile)

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}