package com.example.mobilearek.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilearek.R
import com.example.mobilearek.adapter.AdapterPesanan
import com.example.mobilearek.model.Mobil
import com.example.mobilearek.room.MyDatabase


class PesananActivity : AppCompatActivity() {

    lateinit var rvMobil: RecyclerView
    lateinit var myDb: MyDatabase
    lateinit var mobil: Mobil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pesanan)

        mainButton()

    }
    lateinit var adapter : AdapterPesanan
    private var listMobil = ArrayList<Mobil>()
    private fun displayMobil(){
        listMobil = myDb.daoPesan().getAll() as ArrayList

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        adapter = AdapterPesanan(this,listMobil,object : AdapterPesanan.Listeners{
            override fun onDelete(position: Int) {
                listMobil.removeAt(position)
                adapter.notifyDataSetChanged()
            }

        })
        rvMobil.adapter = adapter
        rvMobil.layoutManager = layoutManager
    }


    fun mainButton() {
        rvMobil = findViewById(R.id.rv_mobil)
        myDb = MyDatabase.getInstance(this)!!

    }


    override fun onResume() {
        super.onResume()
        displayMobil()
    }

}
