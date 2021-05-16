package com.example.mobilearek.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.example.mobilearek.R
import com.example.mobilearek.fragment.HomeFragment
import com.example.mobilearek.model.Mobil
import com.example.mobilearek.room.MyDatabase
import com.example.mobilearek.util.Config
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail_mobil.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.toolbar.*
import java.text.NumberFormat
import java.util.*

class DetailMobilActivity : AppCompatActivity() {
    lateinit var myDb: MyDatabase
    lateinit var mobil: Mobil



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_mobil)
        myDb = MyDatabase.getInstance(this)!!


        getData()
        mainButton()
    }
    fun mainButton(){
        btn_pesan.setOnClickListener{
            insert()
        }
    }

    fun insert(){
        CompositeDisposable().add(Observable.fromCallable { myDb.daoNote().insert(mobil) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {

                Log.d("respons", "data inserted")
            })
    }


    @SuppressLint("SetTextI18n")
    fun getData(){
        val data = intent.getStringExtra("extra")
        mobil = Gson().fromJson<Mobil>(data, Mobil::class.java)

        tv_nama.text = mobil.merk +" "+ mobil.name
        tv_harga.text = NumberFormat.getCurrencyInstance(Locale("in","ID")).format(Integer.valueOf(mobil.harga))
        tv_kapasitas.text = mobil.kapasitas
        tv_tahun.text = mobil.tahun
        tv_transmisi.text = mobil.transmisi
        tv_deskripsi.text = mobil.deskripsi


        val img = Config.urlData + mobil.image
        Picasso.get()
            .load(img)
            .placeholder(R.drawable.ic_baseline_arrow_back_24)
            .error(R.drawable.ic_baseline_close_24)
            .resize(400,400)
            .into(iv_gambar)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = mobil.merk +" "+ mobil.name
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}