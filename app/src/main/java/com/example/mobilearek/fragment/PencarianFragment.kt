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
import android.widget.*
import androidx.cardview.widget.CardView
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
    private lateinit var cari : SearchView
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var rl : RelativeLayout
    private lateinit var pbCari : ProgressBar
    private lateinit var empty : LinearLayout
    private lateinit var pbLoad : ProgressBar
    private lateinit var cv : CardView
    private lateinit var btnLoad : ImageButton
    private lateinit var bgCari : LinearLayout
    private lateinit var llGagal : LinearLayout
    private var page = 1
    private var totalPage = 1
    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_pencarian, container, false)
        layoutManager = GridLayoutManager(activity,2,GridLayoutManager.VERTICAL,false)
        init(view)
        btn()
        search()
        display()
        searchView()
        cv.visibility = View.GONE

        return view
    }
    private fun searchView(){
        rl.setOnClickListener {
         cari.isIconified = false
        }


    }
    private var q = ""
    private var adapter : AdapterMobil? = null
    private var listmobil : ArrayList<Mobil> = ArrayList()
    private fun display(){
        rvCariMobil.setHasFixedSize(true)
        rvCariMobil.layoutManager = layoutManager
        adapter = activity?.let { AdapterMobil(it) }
        rvCariMobil.adapter = adapter
        adapter!!.setData()
    }
    private fun getMobil(isOnReferesh : Boolean){
        isLoading = true
        val query = q
        if (!isOnReferesh){
            pbLoad.visibility = View.VISIBLE
        }else{
            pbCari.visibility = View.VISIBLE
        }
        val param = HashMap<String,String>()
        param["page"] = page.toString()
        ApiConfig.instanceRetrofit.searchMobil(query,param).enqueue(object : Callback<ResponModel> {
            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val respon = response.body()?.page!!.data
                totalPage = response.body()?.page!!.last_page
                if(!respon.isEmpty()) {
                    adapter!!.addList(respon)
                    if (totalPage == page) {
                        cv.visibility = View.GONE
                    } else {
                        cv.visibility = View.VISIBLE
                    }
                    pbCari.visibility = View.GONE
                    pbLoad.visibility = View.GONE
                    Log.e("RESPONS", "1: " + respon.isEmpty())
                }else{
                    Log.e("RESPONS", "3: "+ respon)
                    empty.visibility = View.VISIBLE
                    cv.visibility = View.GONE
                    pbCari.visibility = View.GONE
                    pbLoad.visibility = View.GONE
                }

            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                cv.visibility = View.GONE
                pbLoad.visibility = View.GONE
                pbCari.visibility = View.GONE
                llGagal.visibility = View.VISIBLE
                Log.e("RESPONS", "ERROR: "+t.message)
            }

        })
    }
    private fun search(){
        cari.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                page = 1
                empty.visibility = View.GONE
                bgCari.visibility = View.GONE
                q = query.toString()
                Log.e("QUERY",q + query)
                getMobil(true)
                hideKey(cari)
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if(adapter == null){
                    Toast.makeText(activity, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
                    return true
                }
                empty.visibility = View.GONE
                bgCari.visibility = View.VISIBLE
                llGagal.visibility = View.GONE
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
        btnLoad.setOnClickListener {
            cv.visibility = View.GONE
            pbLoad.visibility = View.VISIBLE
            isLoading = false
            if (!isLoading && page < totalPage ){
                page++
                Log.d("RESPONS", "Page load: "+ page + totalPage)
                getMobil(false)
            }
        }
        
    }
    private fun init(view: View) {
        rvCariMobil = view.findViewById(R.id.rv_carimobil)
        cari = view.findViewById(R.id.ic_cari)
        rl = view.findViewById(R.id.rl_cari)
        pbCari = view.findViewById(R.id.loading_pencarian)
        empty = view.findViewById(R.id.ll_empty_cari)
        cv = view.findViewById(R.id.pc_cv)
        pbLoad =  view.findViewById(R.id.loading_page)
        btnLoad = view.findViewById(R.id.pc_cv_load)
        bgCari = view.findViewById(R.id.ll_bg)
        llGagal = view.findViewById(R.id.cari_gagal)
    }
}