package com.example.mobilearek.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.mobilearek.MainActivity
import com.example.mobilearek.R
import com.example.mobilearek.app.ApiConfig
import com.example.mobilearek.helper.SharedPref
import com.example.mobilearek.model.ResponModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_lupa_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class LupaPasswordActivity : AppCompatActivity() {

    lateinit var s: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lupa_password)

        s= SharedPref(this)

        btn_lpp.setOnClickListener {
            kirim()
        }

        hub_admin.setOnClickListener{
            hubungi()
        }
    }
    private fun kirim(){
        if (lp_email.text!!.isEmpty()){
            lp_email.requestFocus()
            lp_email.error = " Tidak boleh kosong"
            return
        }
        else if (lp_nik.text!!.isEmpty()){
            lp_nik.requestFocus()
            lp_nik.error = " Tidak boleh kosong"
            return
        }
       else if (lp_nik.text!!.length < 15){
            lp_nik.requestFocus()
            lp_nik.error = " NIK tidak valid"
            return
        }
        pb_lpp.visibility = View.VISIBLE
        ApiConfig.instanceRetrofit.lupapass(lp_email.text.toString(),lp_nik.text.toString()).enqueue(object : Callback<ResponModel> {
            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                pb_lpp.visibility = View.GONE
                val respon = response.body()
                if (respon != null) {
                    if (respon.success == 1){
                        s.setUser(respon.user)
                        s.setInt(s.id,respon.user.id)
                        val intent = Intent (this@LupaPasswordActivity,ResetPasswordActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this@LupaPasswordActivity, "Error:" + respon.message, Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this@LupaPasswordActivity, "Tidak ada respon", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                pb_lpp.visibility = View.GONE
                Toast.makeText(this@LupaPasswordActivity, "Error:" + t.message, Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this, "install wa", Toast.LENGTH_SHORT).show()
            }
        }catch (e: Exception){
            Toast.makeText(this, "Error"+e.stackTrace, Toast.LENGTH_SHORT).show()
        }
    }
}