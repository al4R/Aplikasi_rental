package com.example.mobilearek.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mobilearek.R
import com.example.mobilearek.helper.SharedPref
import com.example.mobilearek.model.Transaksi
import com.example.mobilearek.util.Config
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_riwayat.*
import kotlinx.android.synthetic.main.toolbar.*

class DetailRiwayatActivity : AppCompatActivity() {

    lateinit var transaksi: Transaksi
    lateinit var s:SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_riwayat)

        s = SharedPref(this)
        mainButton()
        getData()
    }

    @SuppressLint("SetTextI18n")
    fun getData(){
        val data = intent.getStringExtra("transaksi")
        transaksi = Gson().fromJson<Transaksi>(data, Transaksi::class.java)
        val detail = transaksi.details.mobil
        kode_tr.text = transaksi.kode_tran
        tgl_pesan.text = transaksi.tgl_order
        pemesan.text = s.nama
        val image = Config.urlData + transaksi.details.mobil.image
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.ic_baseline_arrow_back_24)
            .error(R.drawable.ic_baseline_close_24)
            .resize(400, 400)
            .into(dr_mobil)
        rw_mobil.text = "Mobil : " + detail.merk +" "+ detail.name +" "+ detail.tahun
        harga_sewa.text = "Harga sewa: Rp "+detail.harga
        nomorM.text = "Nomor kendaraan : "+detail.no_kendaraan
        tgl_sewa.text = "Tanggal "+transaksi.tgl_sewa
        tglKmbali.text = "Tanggal"+transaksi.tgl_akhir_sewa
        tv_durasi.text = transaksi.lama_sewa+" Jam"
        totalByr.text = "Rp. "+transaksi.total_harga

    }
    fun mainButton(){
        btn_hapus.setOnClickListener {
            hapus()
        }
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Detail Riwayat"
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun hapus() {
        onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}