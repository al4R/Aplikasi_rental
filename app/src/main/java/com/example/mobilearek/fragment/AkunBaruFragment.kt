package com.example.mobilearek.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.mobilearek.R
import com.example.mobilearek.activity.DaftarActivity
import com.example.mobilearek.activity.EditProfileActivity
import com.example.mobilearek.activity.LoginActivity

class AkunBaruFragment : Fragment() {
    lateinit var btnMsk:Button
    lateinit var btnDft:Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_baru, container, false)
        init(view)


        btn()

        return view
    }
    private fun btn(){
        btnMsk.setOnClickListener {
            val intent = Intent (activity,LoginActivity::class.java)
            startActivity(intent)
        }
        btnDft.setOnClickListener {
            val intent = Intent (activity,DaftarActivity::class.java)
            startActivity(intent)
        }
        
    }
    private fun init(view: View) {
        btnMsk=view.findViewById(R.id.btn_msk)
        btnDft=view.findViewById(R.id.btn_dft)

    }

}