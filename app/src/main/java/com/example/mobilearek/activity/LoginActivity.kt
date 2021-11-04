package com.example.mobilearek.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import com.example.mobilearek.MainActivity
import com.example.mobilearek.R
import com.example.mobilearek.app.ApiConfig
import com.example.mobilearek.helper.SharedPref
import com.example.mobilearek.model.ResponModel
import kotlinx.android.synthetic.main.activity_daftar.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.progress_bar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class LoginActivity : AppCompatActivity() {
    lateinit var s:SharedPref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        s= SharedPref(this)
        mainbuuton()
    }
    private fun mainbuuton(){
        val btndaftar = findViewById<Button>(R.id.btn_daftar)
        btndaftar.setOnClickListener {
            val intent= Intent(this,DaftarActivity::class.java)
            startActivity(intent)
            finish()
        }
        btn_close.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        btn_lupa_pass.setOnClickListener {
            val intent= Intent(this,LupaPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }
        btn_hub_admin.setOnClickListener{
            hubungi()
        }
        btn_masuk.setOnClickListener {
            if(et_email.text.toString().isEmpty()){
                et_email.error = "Email tidak valid"
                et_email.requestFocus()
            }else if(et_password.text.toString().isEmpty()) {
                et_password.error = "Pasword tidak boleh kosong"
                et_password.requestFocus()
            }else if(et_password.length() < 7){
                et_password.error = "Minimal 8 karakter"
                et_password.requestFocus()
            }else if(et_email.text.toString().isNotEmpty()){
                if (Patterns.EMAIL_ADDRESS.matcher(et_email.text.toString()).matches() == false) {
                    et_email.error = "Email tidak valid"
                    et_email.requestFocus()
                }
            }else{
                daftar()
            }
        }
    }
    private fun daftar() {
        progress_bar.visibility = View.VISIBLE
        ApiConfig.instanceRetrofit.login(et_email.text.toString(), et_password.text.toString()
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
                        Toast.makeText(this@LoginActivity, "Selamat Datang " + respon.user.name, Toast.LENGTH_SHORT).show()
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this@LoginActivity, "Error:" + respon.message, Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this@LoginActivity, "Tidak ada respon", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                progress_bar.visibility = View.GONE
                Log.e("RESPONS", "ERROR: "+t.message)
            }

        })
    }

    private fun hubungi(){
        try {
            val packageManager = this.packageManager
            val i = Intent(Intent.ACTION_VIEW)
            val url = "https://api.whatsapp.com/send/?phone=+6285747488316?text=Saya%memerlukan%bantuan%untuk%&app_absent=0"
            i.setPackage("com.whatsapp")
            i.data = Uri.parse(url)
            if(i.resolveActivity(packageManager) != null){
                startActivity(i)
            }else{
                Toast.makeText(this, "Install  WhatsApp terlebi dahulu", Toast.LENGTH_SHORT).show()
            }
        }catch (e: Exception){
            Toast.makeText(this, "Error"+e.stackTrace, Toast.LENGTH_SHORT).show()
        }
    }
}