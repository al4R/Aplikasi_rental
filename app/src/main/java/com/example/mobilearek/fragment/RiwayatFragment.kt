package com.example.mobilearek.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilearek.R
import com.example.mobilearek.activity.DetailRiwayatActivity
import com.example.mobilearek.adapter.AdapterRiwayat
import com.example.mobilearek.app.ApiConfig
import com.example.mobilearek.helper.SharedPref
import com.example.mobilearek.model.ResponModel
import com.example.mobilearek.model.Transaksi
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RiwayatFragment : Fragment() {

    private lateinit var rvRiwayat : RecyclerView
    private lateinit var Sc : NestedScrollView
    private lateinit var lin : LinearLayout
    private lateinit var pb : ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_riwayat, container, false)
        init(view)

        return view
    }
    fun init(view: View){
        rvRiwayat = view.findViewById(R.id.rv_riwayat)
        Sc = view.findViewById(R.id.sc)
        lin = view.findViewById(R.id.lL_empty)
        pb = view.findViewById(R.id.loading_riwayat)
    }
    fun getRiwayat(){
        pb.visibility = View.VISIBLE
        val id = SharedPref(requireActivity()).getUser()!!.id
        ApiConfig.instanceRetrofit.getRiwayat(id).enqueue(object : Callback<ResponModel> {
            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val respon = response.body()
                if(respon != null){
                    if (respon.success == 1){
                        pb.visibility = View.GONE
                        displayRiwayat(respon.transaksi)
                        Log.d("RESPONS", "displayData: "+respon.transaksi.size)
                    }
                    if (respon.success == 1 && respon.transaksi.isEmpty()){
                        pb.visibility = View.GONE
                        lin.visibility = View.VISIBLE
                        Sc.visibility = View.GONE
                    }
                }else{
                    lin.visibility = View.VISIBLE
                    Sc.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                pb.visibility = View.GONE
                Log.d("RESPONS", "ERROR: "+t.message)

            }

        })
    }
    fun displayRiwayat(transaksi : ArrayList<Transaksi>){
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rvRiwayat.adapter = AdapterRiwayat(transaksi,object : AdapterRiwayat.Listeners{
            override fun onClicked(data: Transaksi) {
                val json = Gson().toJson(data,Transaksi::class.java)
                val intent = Intent(activity, DetailRiwayatActivity::class.java)
                intent.putExtra("transaksi",json)
                startActivity(intent)
            }

        })
        rvRiwayat.layoutManager = layoutManager

    }

    override fun onResume() {
        if(SharedPref(requireActivity()).getStatusLogin()== false) {
            lin.visibility = View.VISIBLE
            Sc.visibility = View.GONE
        }else {
            getRiwayat()
        }
        super.onResume()
    }
}