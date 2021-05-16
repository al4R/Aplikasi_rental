package com.example.mobilearek.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.mobilearek.MainActivity
import com.example.mobilearek.R
import com.example.mobilearek.app.ApiConfig
import com.example.mobilearek.helper.SharedPref
import com.example.mobilearek.model.ResponModel
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    lateinit var s:SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        s= SharedPref(this)
        val btndaftar = findViewById<Button>(R.id.btn_daftar)

        btn_masuk.setOnClickListener {
            login()
        }
        btndaftar.setOnClickListener {
            val intent= Intent(this,DaftarActivity::class.java)
        startActivity(intent)}
    }

    private fun login() {
        if(edit_email.text!!.isEmpty()) {
            edit_email.error = "Email tidak boleh kosong"
            edit_email.requestFocus()
            return
        }else if(edit_password.text!!.isEmpty()) {
            edit_password.error = "Nama tidak boleh kosong"
            edit_password.requestFocus()
            return
        }

        progress_bar.visibility = View.VISIBLE
        ApiConfig.instanceRetrofit.login(edit_email.text.toString(), edit_password.text.toString()
        ).enqueue(object : Callback<ResponModel> {
            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                progress_bar.visibility = View.GONE
                val respon = response.body()
                if (respon != null) {
                    if (respon.success == 1){
                        s.setStatusLogin(true)
                        s.setUser(respon.user)
                        s.setString(s.nama,respon.user.name)
                        s.setString(s.email,respon.user.email)
                        s.setString(s.telepon,respon.user.telepon)
                        val intent = Intent (this@LoginActivity,MainActivity::class.java)
                        Toast.makeText(this@LoginActivity, "Selamat" + respon.user.name, Toast.LENGTH_SHORT).show()
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this@LoginActivity, "Error:" + respon.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                progress_bar.visibility = View.GONE
                Toast.makeText(this@LoginActivity, "Error:" + t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
}