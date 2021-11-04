package com.example.mobilearek.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.mobilearek.MainActivity
import com.example.mobilearek.R
import com.example.mobilearek.app.ApiConfig
import com.example.mobilearek.helper.SharedPref
import com.example.mobilearek.model.ResponModel
import kotlinx.android.synthetic.main.activity_edit_password.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditPasswordActivity : AppCompatActivity() {

    lateinit var s: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_password)

        s= SharedPref(this)

        mainbutton()
    }
    private fun mainbutton(){
        btn_simpa_pass.setOnClickListener {
           editPass()
        }
        btn_btl_pass.setOnClickListener {
            onBackPressed()
        }
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Ubah kata sandi"
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
    private fun editPass(){
        if(edit_password_lama.text!!.isEmpty()){
            edit_password_lama.error = "Tidak boleh kosong"
            edit_password_lama.requestFocus()
            return
        }else if(edit_password.text!!.isEmpty()){
            edit_password.error = "Tidak boleh kosong"
            edit_password.requestFocus()
            return
        }
        else if (edit_password.length() < 7){
            edit_password.error = "Minimal 8 karakter"
            edit_password.requestFocus()
            return
        }else if (edit_password.text.toString() != edit_konfirmasi.text.toString()){
            edit_password.error = "Tidak cocok"
            edit_konfirmasi.error = "Tidak cocok"
            edit_password.requestFocus()
            return
        }
        val user=s.getUser()
        val id =user!!.id
        ApiConfig.instanceRetrofit.editPass(id,edit_password_lama.text.toString(),edit_password.text.toString())
            .enqueue(object : Callback<ResponModel> { override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val respon = response.body()
                if (respon != null) {
                    if (respon.success == 1){
                        Toast.makeText(this@EditPasswordActivity, "Berhasil mengubah kata sandi", Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    }else{
                        Toast.makeText(this@EditPasswordActivity, "Error:" + respon.message, Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this@EditPasswordActivity, "Tidak ada respon", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                Toast.makeText(this@EditPasswordActivity, "Error:" + t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}