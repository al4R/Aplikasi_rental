package com.example.mobilearek.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.mobilearek.R
import com.example.mobilearek.helper.SharedPref


class AkunFragment : Fragment() {

    lateinit var s:SharedPref
    lateinit var btnLogout:Button

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate (R.layout.fragment_akun,container,false)
        btnLogout = view.findViewById(R.id.btn_logout)

        s =SharedPref(activity!!)

        btnLogout.setOnClickListener {
            s.setStatusLogin(false)
        }
        return view
    }

}