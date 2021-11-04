package com.example.mobilearek.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.mobilearek.R
import com.example.mobilearek.activity.PesananActivity
import com.example.mobilearek.adapter.AdapterMobil
import com.example.mobilearek.app.ApiConfig
import com.example.mobilearek.model.Mobil
import com.example.mobilearek.model.ResponModel
import com.example.mobilearek.room.MyDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private lateinit var rvMobil: RecyclerView
    private lateinit var idBulat : RelativeLayout
    private lateinit var  tvAngka : TextView
    private lateinit var  myDb : MyDatabase
    private lateinit var  icMobbil : ImageView
    private lateinit var  pgBar : ProgressBar
    private lateinit var  pgLoad : ProgressBar
    private lateinit var llGalal : LinearLayout
//    private lateinit var  mobil : Mobil
    private lateinit var Hmcv : CardView
    private lateinit var loadPage : ImageButton
    private lateinit var swipeRefresh : SwipeRefreshLayout
    private lateinit var layoutManager: GridLayoutManager
    private var page = 1
    private var totalPage = 1
    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater.inflate(R.layout.fragment_home, container, false)
        myDb = MyDatabase.getInstance(requireActivity())!!
        layoutManager = GridLayoutManager(activity,2,GridLayoutManager.VERTICAL,false)
        init(view)
        mainButton()
        display()


        return view
    }


//    private var listMobil : ArrayList<Mobil> =ArrayList()
    private var  adapter : AdapterMobil? = null
    private fun getMobil(isOnReferesh : Boolean){
        isLoading = true
        if (!isOnReferesh){
            pgLoad.visibility = View.VISIBLE
        }else{
            pgBar.visibility = View.VISIBLE
        }
        Hmcv.visibility = View.GONE
        llGalal.visibility = View.GONE
        val param = HashMap<String,String>()
        param["page"] = page.toString()
        ApiConfig.instanceRetrofit.getmobilpage(param).enqueue(object : Callback<ResponModel> {
            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
            totalPage = response.body()!!.page.last_page
            val list = response.body()!!.page.data
                adapter!!.addList(list)
                if (totalPage == page){
                    Hmcv.visibility = View.GONE
                }else{
                    Hmcv.visibility = View.VISIBLE
                }
                pgBar.visibility = View.GONE
                pgLoad.visibility = View.GONE
            }
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                Hmcv.visibility = View.GONE
                pgBar.visibility = View.GONE
                pgLoad.visibility = View.GONE
                Log.d("RESPONS", "ERROR: "+t.message)
                llGalal.visibility = View.VISIBLE
            }

        })
    }


    private fun display(){
        rvMobil.setHasFixedSize(true)
        rvMobil.layoutManager = layoutManager
        adapter = activity?.let { AdapterMobil(it) }
        rvMobil.adapter = adapter
    }

    @SuppressLint("NewApi")
    private fun hideKey(view: View){
        val key : InputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        key.hideSoftInputFromWindow(view.windowToken,0)
    }

    private fun check(){
        val data = myDb.daoPesan().getAll()
        if (data.isNotEmpty()){
            idBulat.visibility = View.VISIBLE
            tvAngka.text = data.size.toString()
        }else{
            idBulat.visibility =View.GONE
        }
    }

    private fun mainButton(){
        icMobbil.setOnClickListener{
            val intent = Intent (activity, PesananActivity::class.java)
            startActivity(intent)
        }
        swipeRefresh.setOnRefreshListener {
            adapter?.clear()
            page = 1
            getMobil(true)
            swipeRefresh.isRefreshing = false
        }
        loadPage.setOnClickListener{
            val visibleItem = layoutManager.childCount
            val pastvisible = layoutManager.findFirstVisibleItemPosition()
            val total = adapter!!.itemCount
            isLoading = false
            Hmcv.visibility = View.GONE
            pgLoad.visibility = View.VISIBLE
            Log.d("RESPONS", "Page: "+ page+" "+visibleItem+" "+pastvisible+" "+total+" "+isLoading)
            if (!isLoading && page < totalPage ){
                page++
                Log.d("RESPONS", "Page load: "+ page + totalPage)
                getMobil(false)
            }
        }
    }
    private fun init(view: View){
        rvMobil = view.findViewById(R.id.rv_mobil)
        idBulat = view.findViewById(R.id.id_bulat)
        tvAngka = view.findViewById(R.id.tv_angka)
        icMobbil = view.findViewById(R.id.ic_mobil)
        pgBar = view.findViewById(R.id.pg_bar)
        swipeRefresh = view.findViewById(R.id.swipeRefresh)
        loadPage = view.findViewById(R.id.cv_load)
        pgLoad = view.findViewById(R.id.pg_bar_page)
        Hmcv = view.findViewById(R.id.hm_cv)
        llGalal = view.findViewById(R.id.home_gagal)
    }


    override fun onResume() {
        adapter?.clear()
        page = 1
        getMobil(true)
        check()
//        search()
        super.onResume()
    }


}

//        rvMobil.addOnScrollListener(object : RecyclerView.OnScrollListener(){
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                Log.e("RECYCLER",dy.toString())
//                if(dy > 0){
//                    val visibleItem = LayoutManager.childCount
//                    val pastvisible = LayoutManager.findFirstVisibleItemPosition()
//                    val total = adapter!!.itemCount
//
//                    Log.d("RESPONS", "Page: "+ page+" "+visibleItem+" "+pastvisible+" "+total+" ")
//                    if (!isLoading && page < totalPage ){
//                        if(visibleItem + pastvisible >= total){
//                            page++
//                            Log.d("RESPONS", "Page: "+ page)
//                            getMobil(false)
//
//                        }
//                    }
//                }
//                super.onScrolled(recyclerView, dx, dy)
//            }
//        })

//    fun search(){
//        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
//            override fun onQueryTextSubmit(query: String?): Boolean {
////               adapter!!.filter.filter(query)
//
//                hideKey(search)
//
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                if(adapter == null){
//                    Toast.makeText(activity, "Data ksosng", Toast.LENGTH_SHORT).show()
//                    return true
//                }
//                adapter!!.filter.filter(newText)
//                return true
//            }
//        })
//    }
