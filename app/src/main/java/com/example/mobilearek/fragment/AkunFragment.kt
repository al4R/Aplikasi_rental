package com.example.mobilearek.fragment


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.mobilearek.MainActivity
import com.example.mobilearek.R
import com.example.mobilearek.activity.EditProfileActivity
import com.example.mobilearek.helper.SharedPref



class AkunFragment : Fragment() {

    private lateinit var s:SharedPref
    private lateinit var btnLogout:Button
    private lateinit var btnEditPr:Button

    private lateinit var tvNama:TextView
    private lateinit var tvEmail:TextView
    private lateinit var tvTelepon:TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate (R.layout.fragment_akun,container,false)

        init(view)

        s = SharedPref(requireActivity())

        btnLogout.setOnClickListener {
            s.setStatusLogin(false)
            val intent = Intent (activity,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        btnEditPr.setOnClickListener{
            val intent = Intent (activity, EditProfileActivity::class.java)
            startActivity(intent)
        }

        setData()
        return view
    }
    @SuppressLint("SetTextI18n")
    fun setData() {
        val user = s.getUser()
        if (user != null) {
            tvNama.text = user.name
            tvEmail.text = user.email
            tvTelepon.setText(user.telepon)

        }else{
            tvNama.text = "Nama"
            tvEmail.text = "Email"
            tvTelepon.text = "Telepon"
        }

        

    }
     fun init(view: View) {
        btnLogout = view.findViewById(R.id.btn_logout)
        tvNama = view.findViewById(R.id.tv_nama)
        tvEmail = view.findViewById(R.id.tv_email)
        tvTelepon = view.findViewById(R.id.tv_telepon)
        btnEditPr=view.findViewById(R.id.btn_editProfile)

    }

}