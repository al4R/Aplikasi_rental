package com.example.mobilearek.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
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
    private lateinit var  mobil : Mobil
    private lateinit var SwipeRefresh : SwipeRefreshLayout
    private lateinit var LayoutManager: GridLayoutManager
    private var page = 1
    private var totalPage = 1
    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater.inflate(R.layout.fragment_home, container, false)
        myDb = MyDatabase.getInstance(requireActivity())!!
        LayoutManager = GridLayoutManager(activity,2,GridLayoutManager.VERTICAL,false)
        init(view)
        mainButton()
        display()

        rvMobil.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                Log.e("RECYCLER",dy.toString())
                if(dy > 0){
                    val visibleItem = LayoutManager.childCount
                    val pastvisible = LayoutManager.findFirstVisibleItemPosition()
                    val total = adapter!!.itemCount

                    Log.d("RESPONS", "Page: "+ page+" "+visibleItem+" "+pastvisible+" "+total+" ")
                    if (!isLoading && page < totalPage ){
                        if(visibleItem + pastvisible >= total){
                            page++
                            Log.d("RESPONS", "Page: "+ page)
                            getMobil(false)

                        }
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })


        return view
    }


//    private var listMobil : ArrayList<Mobil> =ArrayList()
    private var  adapter : AdapterMobil? = null
    private fun getMobil(isOnReferesh : Boolean){
        isLoading = true
        if (!isOnReferesh)pgBar.visibility = View.VISIBLE
        pgBar.visibility = View.VISIBLE
        val param = HashMap<String,String>()
        param["page"] = page.toString()
        ApiConfig.instanceRetrofit.getmobilpage(param).enqueue(object : Callback<ResponModel> {
            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
            totalPage = response.body()?.page!!.last_page
            val list = response.body()?.page?.data
            if (list != null){
                adapter!!.addList(list)

            }else{
                Toast.makeText(activity, "Data Kosong", Toast.LENGTH_SHORT).show()
            }
               Handler().postDelayed({
                   pgBar.visibility = View.GONE
                   isLoading = false
                   SwipeRefresh.isRefreshing = false
               },3000)
            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                pgBar.visibility = View.GONE
                Log.d("RESPONS", "ERROR: "+t.message)
//                Toast.makeText(activity, "Error: "+t.message, Toast.LENGTH_SHORT).show()

            }

        })
    }
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

    fun display(){
        rvMobil.setHasFixedSize(true)
        rvMobil.layoutManager = LayoutManager
        adapter = activity?.let { AdapterMobil(it) }
        rvMobil.adapter = adapter
//        rvMobil.adapter = AdapterMobil(requireActivity(),listMobil)
//        rvMobil.layoutManager = layoutManager
    }
    private fun hideKey(view: View){
        val key : InputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        key.hideSoftInputFromWindow(view.windowToken,0)
    }

    fun check(){
        val data = myDb.daoPesan().getAll()
        if (data.isNotEmpty()){
            idBulat.visibility = View.VISIBLE
            tvAngka.text = data.size.toString()
        }else{
            idBulat.visibility =View.GONE
        }
    }

    fun mainButton(){
        icMobbil.setOnClickListener{
            val intent = Intent (activity, PesananActivity::class.java)
            startActivity(intent)
        }
        SwipeRefresh.setOnRefreshListener {
            adapter?.clear()
            page = 1
            getMobil(true)
            SwipeRefresh.isRefreshing = false
        }
    }
    private fun init(view: View){
        rvMobil = view.findViewById(R.id.rv_mobil)
        idBulat = view.findViewById(R.id.id_bulat)
        tvAngka = view.findViewById(R.id.tv_angka)
        icMobbil = view.findViewById(R.id.ic_mobil)
        pgBar = view.findViewById(R.id.pg_bar)
        SwipeRefresh = view.findViewById(R.id.swipeRefresh)

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


