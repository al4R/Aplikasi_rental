package com.example.mobilearek.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Layout
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilearek.R
import com.example.mobilearek.activity.DetailRiwayatActivity
import com.example.mobilearek.activity.DetailTransaksiActivity
import com.example.mobilearek.adapter.AdapterPembayaran
import com.example.mobilearek.adapter.AdapterRiwayat
import com.example.mobilearek.app.ApiConfig
import com.example.mobilearek.helper.SharedPref
import com.example.mobilearek.model.DetailTransaksi
import com.example.mobilearek.model.ResponModel
import com.example.mobilearek.model.Transaksi
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_pesanan.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PesananFragment : Fragment() {

    private lateinit var rvTransaksi : RecyclerView
    private lateinit var Scrv : NestedScrollView
    private lateinit var Empty : LinearLayout
    private lateinit var Pb : ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_pesanan, container, false)
        init(view)
        return view
    }
    fun init(view: View) {
        rvTransaksi = view.findViewById(R.id.rv_transaksi)
        Scrv = view.findViewById(R.id.nest_scrol)
        Empty = view.findViewById(R.id.ll_empty)
        Pb = view.findViewById(R.id.loading_bayar)
    }
    fun getTransaksi(){
        Pb.visibility = View.VISIBLE
        val id = SharedPref(requireActivity()).getUser()!!.id
        ApiConfig.instanceRetrofit.getTransaksi(id).enqueue(object : Callback<ResponModel> {
            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val respon = response.body()
                if(respon != null){
                    if (respon.success == 1){
                        Pb.visibility = View.GONE
                        displayRiwayat(respon.transaksi)
                        Log.d("RESPONS", "displayData: "+respon.transaksi.size)
                    }
                    if (respon.success == 1 && respon.transaksi.isEmpty()){
                        Pb.visibility = View.GONE
                        Scrv.visibility = View.GONE
                        Empty.visibility = View.VISIBLE
                    }
                }
            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                Log.d("RESPONS", "ERROR: "+t.message)

            }
        })
    }
    fun displayRiwayat(transaksi : ArrayList<Transaksi>){
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rvTransaksi.adapter = activity?.let {
            AdapterPembayaran(it,transaksi,object : AdapterPembayaran.Listeners{
                override fun onClicked(data: Transaksi) {
                    val json = Gson().toJson(data, Transaksi::class.java)
                    val intent = Intent(activity, DetailTransaksiActivity::class.java)
                    intent.putExtra("transaksi",json)
                    startActivity(intent)
                }

            })
        }
        rvTransaksi.layoutManager = layoutManager

    }

    override fun onResume() {
        if(SharedPref(requireActivity()).getStatusLogin()== false) {
            Scrv.visibility = View.GONE
            Empty.visibility = View.VISIBLE
        }else {
            getTransaksi()
        }
        super.onResume()
    }
}

