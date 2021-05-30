package com.example.mobilearek.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {
    private lateinit var rvMobil: RecyclerView
    lateinit var idBulat : RelativeLayout
    lateinit var tvAngka : TextView
    lateinit var myDb : MyDatabase
    lateinit var icMobbil : ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater.inflate(R.layout.fragment_home, container, false)
        myDb = MyDatabase.getInstance(requireActivity())!!

        init(view)
        mainButton()

        return view
    }
    fun display(){
        val layoutManager = GridLayoutManager (activity,2,GridLayoutManager.VERTICAL,false)
        rvMobil.adapter = AdapterMobil(requireActivity(),listMobil)
        rvMobil.layoutManager = layoutManager
    }
    private var listMobil:ArrayList<Mobil> = ArrayList()
    private var displayList = ArrayList<Mobil>()

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
                Toast.makeText(activity, "Error:" + t.message, Toast.LENGTH_SHORT).show()
            }

        })
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
    }
    fun init(view: View){
        rvMobil = view.findViewById(R.id.rv_mobil)
        idBulat = view.findViewById(R.id.id_bulat)
        tvAngka = view.findViewById(R.id.tv_angka)
        icMobbil = view.findViewById(R.id.ic_mobil)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu,menu)

        val search = menu.findItem(R.id.menu_search)

       if (search != null){
           val searchView = search.actionView as androidx.appcompat.widget.SearchView
           searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
               override fun onQueryTextSubmit(query: String?): Boolean {
                   return true
               }

               override fun onQueryTextChange(newText: String?): Boolean {
                   if(newText!!.isNotEmpty()){
                       displayList.clear()
                       val search = newText.toLowerCase(Locale.getDefault())
                       listMobil.forEach{
                           if (it.name.toLowerCase(Locale.getDefault()).contains(search)){
                               displayList.add(it)
                           }
                       }
                       rvMobil.adapter!!.notifyDataSetChanged()
                   }else{
                       displayList.clear()
                       displayList.addAll(listMobil)
                       rvMobil.adapter!!.notifyDataSetChanged()
                   }

                   return true
               }

           })
       }
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onResume() {
        super.onResume()
        check()
        getMobil()
    }
}


