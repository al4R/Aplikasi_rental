package com.example.mobilearek.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.example.mobilearek.MainActivity
import com.example.mobilearek.R
import com.example.mobilearek.helper.SharedPref
import kotlinx.android.synthetic.main.fragment_riwayat.*

class RiwayatFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_riwayat, container, false)



        return view
    }

}