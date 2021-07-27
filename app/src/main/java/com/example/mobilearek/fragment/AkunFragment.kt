package com.example.mobilearek.fragment


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.mobilearek.MainActivity
import com.example.mobilearek.R
import com.example.mobilearek.activity.EditPasswordActivity
import com.example.mobilearek.activity.EditProfileActivity
import com.example.mobilearek.activity.ProfilImageActivity
import com.example.mobilearek.activity.SettingActivity
import com.example.mobilearek.helper.SharedPref
import com.example.mobilearek.util.Config
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profil_image.*


class AkunFragment : Fragment() {

    private lateinit var s:SharedPref
    private lateinit var btnLogout:Button
    private lateinit var btnEditPr:Button

    private lateinit var tvNama:TextView
    private lateinit var tvEmail:TextView
    private lateinit var tvTelepon:TextView
    private lateinit var btnProfil:ImageView
    private lateinit var btnPass:Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate (R.layout.fragment_akun,container,false)

        init(view)

        s = SharedPref(requireActivity())


        mainbutton()
        return view
    }
    @SuppressLint("SetTextI18n")
    fun setData() {
        val user = s.getUser()
        if (user != null) {
            tvNama.text = user.name
            tvEmail.text = user.email
            tvTelepon.text = user.telepon
            if(user.image == ""){
             btnProfil.setImageResource(R.drawable.ic_baseline_person_24)
             return
            }else{
                val image = Config.urlProfil + user.image
                Picasso.get()
                    .load(image)
                    .placeholder(R.drawable.ic_baseline_arrow_back_24)
                    .error(R.drawable.ic_baseline_close_24)
                    .resize(400, 400)
                    .into(btnProfil)
            }
        }
    }
    fun mainbutton(){
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
        btnProfil.setOnClickListener {
            val intent = Intent (activity, ProfilImageActivity::class.java)
            startActivity(intent)
        }
        btnPass.setOnClickListener {
            val intent = Intent (activity, EditPasswordActivity::class.java)
            startActivity(intent)
        }
    }


    fun init(view: View) {
        btnLogout = view.findViewById(R.id.btn_logout)
        tvNama = view.findViewById(R.id.tv_nama)
        tvEmail = view.findViewById(R.id.tv_email)
        tvTelepon = view.findViewById(R.id.tv_telepon)
        btnEditPr=view.findViewById(R.id.btn_editProfile)
        btnProfil=view.findViewById(R.id.pilih_gmbr)
        btnPass=view.findViewById(R.id.btn_edt_pass)

    }

    override fun onResume() {
        setData()
        super.onResume()
    }

}