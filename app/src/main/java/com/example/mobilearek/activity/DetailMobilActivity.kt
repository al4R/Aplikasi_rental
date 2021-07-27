package com.example.mobilearek.activity


import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.mobilearek.R
import com.example.mobilearek.helper.SharedPref
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
import kotlinx.android.synthetic.main.toolbar.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "NAME_SHADOWING")
class DetailMobilActivity : AppCompatActivity() {
    lateinit var myDb: MyDatabase
    lateinit var mobil: Mobil
    lateinit var s:SharedPref




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_mobil)
        myDb = MyDatabase.getInstance(this)!!
        s = SharedPref(this)
        val data = intent.getStringExtra("extra")
        mobil = Gson().fromJson<Mobil>(data, Mobil::class.java)

        getData()
        if (s.getStatusLogin() == false){
            btn_pesan.isEnabled = false
            tv_warning.visibility = View.VISIBLE
        }else if(mobil.status == 1){
            btn_pesan.isEnabled = false
        }else if(mobil.status == 2){
            btn_pesan.isEnabled = false
        }
        btn_pesan.setOnClickListener {
            val data = myDb.daoPesan().getMobil(mobil.id)
            if(data == null){
                val intent = Intent(this, BokingActivity::class.java)
                val str = Gson().toJson(mobil,Mobil::class.java)
                intent.putExtra("extra",str)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this, "Periksa daftar pesanan anda", Toast.LENGTH_SHORT).show()
            }

        }
    }


    @SuppressLint("SetTextI18n")
    fun getData(){
        val data = intent.getStringExtra("extra")
        mobil = Gson().fromJson<Mobil>(data, Mobil::class.java)
        bk_mobil.text = mobil.merk +" "+ mobil.model
        bk_harga.text = "Rp. "+mobil.harga+"/jam"
//            NumberFormat.getCurrencyInstance(Locale("in","ID")).format(Integer.valueOf(mobil.harga))
        bk_kapasitas.text = mobil.kapasitas+" Orang"
        bk_tahun.text = mobil.tahun
        bk_transmisi.text = mobil.transmisi

        deskripsi.text = mobil.deskripsi


        val img = Config.urlData + mobil.image
        Picasso.get()
            .load(img)
            .placeholder(R.drawable.ic_baseline_arrow_back_24)
            .error(R.drawable.ic_baseline_close_24)
            .into(bk_gambar)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = mobil.merk +" "+ mobil.model
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}