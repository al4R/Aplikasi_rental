package com.example.mobilearek.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobilearek.R
import com.example.mobilearek.activity.DetailRiwayatActivity
import com.example.mobilearek.adapter.AdapterRiwayat
import com.example.mobilearek.app.ApiConfig
import com.example.mobilearek.helper.SharedPref
import com.example.mobilearek.model.ResponModel
import com.example.mobilearek.model.Transaksi
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_riwayat.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RiwayatFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_riwayat, container, false)
        return view
    }
//    fun getRiwayat(){
//        val id = SharedPref(requireActivity()).getUser()!!.id
//        ApiConfig.instanceRetrofit.getRiwayat(id).enqueue(object : Callback<ResponModel> {
//            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
//                val respon = response.body()
//                if(respon != null){
//                    if (respon.success == 1){
//                        displayRiwayat(respon.transaksi)
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
//
//            }
//
//        })
//    }
//    fun displayRiwayat(transaksi: ArrayList<Transaksi>){
//        val layoutManager = LinearLayoutManager(activity)
//        layoutManager.orientation = LinearLayoutManager.VERTICAL
//
//        rv_riwayat.adapter = AdapterRiwayat(transaksi,object : AdapterRiwayat.Listeners{
//            override fun onClicked(data: Transaksi) {
//                val json = Gson().toJson(data,Transaksi::class.java)
//                val intent = Intent(activity, DetailRiwayatActivity::class.java)
//                intent.putExtra("transaksi",json)
//                startActivity(intent)
//            }
//
//        })
//    }
//
//    override fun onResume() {
//        getRiwayat()
//        super.onResume()
//    }
}