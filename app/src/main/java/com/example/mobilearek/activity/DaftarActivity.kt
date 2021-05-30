package com.example.mobilearek.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.mobilearek.R
import com.example.mobilearek.app.ApiConfig
import kotlinx.android.synthetic.main.activity_daftar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.Toast
import com.example.mobilearek.helper.SharedPref
import com.example.mobilearek.model.ResponModel
import kotlinx.android.synthetic.main.activity_daftar.btn_daftar
import kotlinx.android.synthetic.main.activity_daftar.edit_email
import kotlinx.android.synthetic.main.activity_daftar.edit_password
import kotlinx.android.synthetic.main.activity_daftar.progress_bar
import kotlinx.android.synthetic.main.activity_login.*


class DaftarActivity : AppCompatActivity() {

    lateinit var s:SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar)

        s= SharedPref(this)

        btn_daftar.setOnClickListener {
            daftar()
        }
    }


     fun daftar() {
        if (edit_nama.text!!.isEmpty()) {
            edit_nama.error = "Kolom Nama tidak boleh kosong"
            edit_nama.requestFocus()
            return
        } else if(edit_email.text!!.isEmpty()) {
            edit_email.error = "Email tidak boleh kosong"
            edit_email.requestFocus()
            return
        }else if(edit_telepon.text!!.isEmpty()) {
            edit_telepon.error = "Telepon tidak boleh kosong"
            edit_telepon.requestFocus()
            return
        }else if(edit_nik.text!!.isEmpty()) {
            edit_nik.error = "NIK tidak boleh kosong"
            edit_nik.requestFocus()
            return
        }else if(edit_password.text!!.isEmpty()) {
            edit_password.error = "Nama tidak boleh kosong"
            edit_password.requestFocus()
            return
        }

        progress_bar.visibility = View.VISIBLE
        ApiConfig.instanceRetrofit.register(
           edit_nama.text.toString(),
           edit_email.text.toString(),
           edit_password.text.toString(),
           edit_telepon.text.toString(),
           edit_nik.text.toString()

        ).enqueue(object : Callback<ResponModel> {
            override fun onResponse(call: Call<ResponModel >, response: Response<ResponModel>) {
                progress_bar.visibility = View.GONE
                val respon = response.body()
                if (respon != null) {
                    if (respon.success == 1){
                        s.setStatusLogin(true)
                                s.setString(s.nama,respon.user.name)
                                s.setString(s.email,respon.user.email)
                                s.setString(s.telepon,respon.user.telepon)
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
}