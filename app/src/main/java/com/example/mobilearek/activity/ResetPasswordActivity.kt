package com.example.mobilearek.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.mobilearek.R
import com.example.mobilearek.app.ApiConfig
import com.example.mobilearek.helper.SharedPref
import com.example.mobilearek.model.ResponModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_reset_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetPasswordActivity : AppCompatActivity() {

    lateinit var s: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        s= SharedPref(this)

        rp_reset.setOnClickListener {
            reset()
        }
    }

    private fun reset(){
        if(rp_password.text!!.length < 7 ){
            rp_password.requestFocus()
            rp_password.error = "minimal 8 karakter"
            return
        } else if(rp_password.text.toString() != rp_confirm.text.toString()){
            rp_password.requestFocus()
            rp_password.error = "Periksa password anda"
            return
        }
        val user = s.getUser()
        val id = user!!.id
        pb_rp.visibility = View.VISIBLE
        ApiConfig.instanceRetrofit.resetpass(id,rp_password.text.toString()).enqueue(object : Callback<ResponModel> {
            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                pb_rp.visibility = View.GONE
                val respon = response.body()
                if (respon != null) {
                    if (respon.success == 1){
                        val intent = Intent(this@ResetPasswordActivity,LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this@ResetPasswordActivity, "Error:" + respon.message, Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this@ResetPasswordActivity, "Tidak ada respon", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                pb_rp.visibility = View.GONE
                Toast.makeText(this@ResetPasswordActivity, "Error" + t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}