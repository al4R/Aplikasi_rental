package com.example.mobilearek.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Adapter
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.mobilearek.R
import com.example.mobilearek.activity.DaftarActivity
import com.example.mobilearek.activity.EditProfileActivity
import com.example.mobilearek.activity.LoginActivity
import com.example.mobilearek.adapter.AdapterMobil
import com.example.mobilearek.app.ApiConfig
import com.example.mobilearek.model.Mobil
import com.example.mobilearek.model.ResponModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PencarianFragment : Fragment() {
    private lateinit var rvCariMobil : RecyclerView
    private lateinit var Swipe : SwipeRefreshLayout
    private lateinit var Cari : SearchView
    private lateinit var LayoutManager: GridLayoutManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_pencarian, container, false)
        LayoutManager = GridLayoutManager(activity,2,GridLayoutManager.VERTICAL,false)
        init(view)
        btn()
        search()
        display()
        Cari.queryHint = "Cari Disini"
        return view
    }
    private var q = ""
    private var adapter : AdapterMobil? = null
    private var listmobil : ArrayList<Mobil> = ArrayList()
    private fun display(){
        rvCariMobil.setHasFixedSize(true)
        rvCariMobil.layoutManager = LayoutManager
        adapter = activity?.let { AdapterMobil(it) }
        rvCariMobil.adapter = adapter
        adapter!!.setData()
    }
    private fun getMobil(isOnReferesh : Boolean){
        val query = q
        if (!isOnReferesh)
        ApiConfig.instanceRetrofit.searchMobil(query).enqueue(object : Callback<ResponModel> {
            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val respon = response.body()
                if(respon != null){
                    if (respon.success == 1){
                        adapter?.addList(respon.mobil)

                        Log.d("RESPONS", "displayData: "+respon.mobil)
                    }
                    if (respon.success == 1 && respon.mobil.isEmpty()){

                    }
                }else{

                }

            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                Log.d("RESPONS", "ERROR: "+t.message)
            }

        })
    }
    fun search(){
        Cari.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                q = query.toString()
                Log.e("QUERY",q + query)
//               adapter!!.filter.filter(query)
                getMobil(false)
                hideKey(Cari)

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(adapter == null){
                    Toast.makeText(activity, "Data ksosng", Toast.LENGTH_SHORT).show()
                    return true
                }
                adapter!!.filter.filter(newText)
                return true
            }
        })
    }
    private fun hideKey(view: View){
        val key : InputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        key.hideSoftInputFromWindow(view.windowToken,0)
    }
    private fun btn(){
        Swipe.setOnRefreshListener {
            Swipe.isRefreshing = false
        }
        
    }
    private fun init(view: View) {
        rvCariMobil = view.findViewById(R.id.rv_carimobil)
        Swipe = view.findViewById(R.id.swRefresh)
        Cari = view.findViewById(R.id.ic_cari)

    }

    override fun onResume() {
        super.onResume()
    }

}