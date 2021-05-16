package com.example.mobilearek.activity


import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mobilearek.R
import com.example.mobilearek.app.ApiConfig
import com.example.mobilearek.helper.SharedPref
import kotlinx.android.synthetic.main.activity_edit_profile.*



class EditProfileActivity : AppCompatActivity() {
    private lateinit var s: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        s = SharedPref(this)

        edit()
    }
    @SuppressLint("SetTextI18n")
    fun edit() {
        val user = s.getUser()
        if (user != null) {
            et_email.setText(user.email)
            et_nama.setText(user.name)
            et_telepon.setText(user.telepon)
        }else{
            et_email.setText("email kosong")
            et_nama.setText("nama kosong")
            et_telepon.setText("telepon kosong")
        }


    }

}