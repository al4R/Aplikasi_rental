package com.example.mobilearek.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilearek.R
import com.example.mobilearek.adapter.AdapterMobil
import com.example.mobilearek.app.ApiConfig
import com.example.mobilearek.model.Mobil
import com.example.mobilearek.model.ResponModel
import com.example.mobilearek.room.MyDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private lateinit var rvMobil: RecyclerView
    lateinit var idBulat : RelativeLayout
    lateinit var tvAngka : TextView
    lateinit var myDb : MyDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater.inflate(R.layout.fragment_home, container, false)
        myDb = MyDatabase.getInstance(requireActivity())!!
        rvMobil = view.findViewById(R.id.rv_mobil)
        idBulat = view.findViewById(R.id.id_bulat)
        tvAngka = view.findViewById(R.id.tv_angka)

        getMobil()

        return view
    }
    fun display(){
        val layoutManager = GridLayoutManager (activity,2,GridLayoutManager.VERTICAL,false)
        rvMobil.adapter = AdapterMobil(requireActivity(),listMobil)
        rvMobil.layoutManager = layoutManager
    }
    private var listMobil:ArrayList<Mobil> = ArrayList()

    private fun getMobil(){
        ApiConfig.instanceRetrofit.getMobil().enqueue(object : Callback<ResponModel> {
            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
            val respon = response.body()
                if (respon!!.success == 1) {
                    listMobil =respon.mobil
                    display()
                }
            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

        })
    }

    fun check(){
        val data = myDb.daoNote().getAll()
        if (data.isNotEmpty()){
            idBulat.visibility = View.VISIBLE
            tvAngka.text = data.size.toString()
        }else{
            idBulat.visibility =View.GONE
        }
    }

    override fun onResume() {
        check()
        super.onResume()
    }
}