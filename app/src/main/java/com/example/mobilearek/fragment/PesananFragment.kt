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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.mobilearek.R
import com.example.mobilearek.activity.DetailTransaksiActivity
import com.example.mobilearek.adapter.AdapterPembayaran
import com.example.mobilearek.app.ApiConfig
import com.example.mobilearek.helper.SharedPref
import com.example.mobilearek.model.ResponModel
import com.example.mobilearek.model.Transaksi
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PesananFragment : Fragment() {

    private lateinit var rvTransaksi : RecyclerView
    private lateinit var Scrv : NestedScrollView
    private lateinit var Empty : LinearLayout
    private lateinit var Pb : ProgressBar
    private lateinit var Swp : SwipeRefreshLayout
    private lateinit var llGagal : LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_pesanan, container, false)

        init(view)
        refresh()
        return view
    }

    private fun refresh() {
        Swp.setOnRefreshListener {
            getTransaksi()
            Toast.makeText(activity, "Refresh", Toast.LENGTH_SHORT).show()
            Swp.isRefreshing = false
        }
    }

    private fun init(view: View) {
        rvTransaksi = view.findViewById(R.id.rv_transaksi)
        Scrv = view.findViewById(R.id.nest_scrol)
        Empty = view.findViewById(R.id.ll_empty)
        Pb = view.findViewById(R.id.loading_bayar)
        Swp = view.findViewById(R.id.swipe)
        llGagal = view.findViewById(R.id.pesan_gagal)
    }
    private fun getTransaksi(){
        llGagal.visibility = View.GONE
        Empty.visibility = View.GONE
        Pb.visibility = View.VISIBLE
        rvTransaksi.visibility = View.GONE
        val id = SharedPref(requireActivity()).getUser()!!.id
        ApiConfig.instanceRetrofit.getTransaksi(id).enqueue(object : Callback<ResponModel> {
            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val respon = response.body()!!
                if(!respon.transaksi.isEmpty()){
                    Pb.visibility = View.GONE
                    rvTransaksi.visibility = View.VISIBLE
                    displayPembayaran(respon.transaksi)
                    Log.d("RESPONS", "displayData: "+respon.transaksi.size)
                }else{
                    Pb.visibility = View.GONE
                    Scrv.visibility = View.GONE
                    Empty.visibility = View.VISIBLE
                }
            }
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                Log.d("RESPONS", "ERROR: "+t.message)
                Pb.visibility = View.GONE
                llGagal.visibility = View.VISIBLE

            }
        })
    }
    private fun displayPembayaran(transaksi : ArrayList<Transaksi>){
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

