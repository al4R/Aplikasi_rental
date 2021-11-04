package com.example.mobilearek.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilearek.R
import com.example.mobilearek.adapter.AdapterPesanan
import com.example.mobilearek.helper.SharedPref
import com.example.mobilearek.model.Mobil
import com.example.mobilearek.room.DaoPesanan
import com.example.mobilearek.room.MyDatabase
import kotlinx.android.synthetic.main.activity_pesanan.*
import kotlinx.android.synthetic.main.empty.*
import kotlinx.android.synthetic.main.toolbar.*


class PesananActivity : AppCompatActivity() {

    lateinit var rvMobil: RecyclerView
    lateinit var myDb: MyDatabase
    lateinit var mobil: Mobil
    lateinit var s : SharedPref
    lateinit var adapter : AdapterPesanan
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pesanan)
        s= SharedPref(this)
        mainButton()

    }

    private var listMobil = ArrayList<Mobil>()
    private fun displayMobil(){
       if (s.getStatusLogin()==false){
           myDb.daoPesan().deleteAll()
       }else{
           listMobil = myDb.daoPesan().getAll() as ArrayList
           val layoutManager = LinearLayoutManager(this)
           layoutManager.orientation = LinearLayoutManager.VERTICAL
           adapter = AdapterPesanan(this,listMobil,object : AdapterPesanan.Listeners{
               @SuppressLint("NotifyDataSetChanged")
               override fun onDelete(position: Int) {
                   listMobil.removeAt(position)
                   adapter.notifyDataSetChanged()
               }
           })
           rvMobil.adapter = adapter
           rvMobil.layoutManager = layoutManager
       }
    }

    fun mainButton() {
        rvMobil = findViewById(R.id.rv_mobil)
        myDb = MyDatabase.getInstance(this)!!

        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Daftar pesanan"
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onResume() {
        val data = myDb.daoPesan().getAll()
        if(data.isEmpty()){
            empty.visibility = View.VISIBLE
            scrol_pesan.visibility = View.GONE
        }else{
            displayMobil()
        }
        super.onResume()
    }

}
