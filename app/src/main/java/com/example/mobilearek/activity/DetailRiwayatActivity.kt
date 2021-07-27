package com.example.mobilearek.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.mobilearek.R
import com.example.mobilearek.app.ApiConfig
import com.example.mobilearek.helper.SharedPref
import com.example.mobilearek.model.Mobil
import com.example.mobilearek.model.ResponModel
import com.example.mobilearek.model.Transaksi
import com.example.mobilearek.util.Config
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_riwayat.*
import kotlinx.android.synthetic.main.activity_detail_riwayat.tgl_pesan
import kotlinx.android.synthetic.main.activity_detail_riwayat.tgl_sewa
import kotlinx.android.synthetic.main.activity_detail_riwayat.totalByr
import kotlinx.android.synthetic.main.activity_detail_riwayat.tv_durasi
import kotlinx.android.synthetic.main.activity_detail_transaksi.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        kode_tr.text = transaksi.kode_tran
        tgl_pesan.text = transaksi.tgl_order
        pemesan.text = s.getUser()!!.name
        val bukti = Config.urlBukti + transaksi.bukti_tf
        Picasso.get()
            .load(bukti)
            .placeholder(R.drawable.ic_baseline_file_copy_24)
            .error(R.drawable.ic_baseline_close_24)
            .resize(400, 400)
            .into(iv_bukti)
        val image = Config.urlData + transaksi.mobil.image
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.ic_baseline_directions_car_24)
            .error(R.drawable.ic_baseline_close_24)
            .resize(400, 400)
            .into(dr_mobil)
        rw_mobil.text = "Mobil : " + transaksi.mobil.merk +" "+ transaksi.mobil.model +" "+ transaksi.mobil.tahun
        harga_sewa.text = "Harga sewa: Rp "+transaksi.mobil.harga
        nomorM.text = "Nomor kendaraan : "+transaksi.mobil.no_kendaraan
        tgl_sewa.text = "Tanggal "+transaksi.tgl_sewa
        tglKmbali.text = "Tanggal "+transaksi.tgl_akhir_sewa
        tv_durasi.text = transaksi.lama_sewa
        totalByr.text = "Rp. "+transaksi.total_harga

    }
    private fun mainButton(){
        btn_hapus.setOnClickListener {
            alertDialog()
        }
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Detail Riwayat"
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
    private fun alertDialog(){
        val alert = AlertDialog.Builder(this)
        alert.setTitle("Apakah anda yakin !")
        alert.setMessage("Ingin membatalkan pesanan ?")
        alert.setPositiveButton("Yes",{  _, _->
            hapus()
        })
        alert.setNegativeButton("No",{ _, _->

        })
        alert.show()
    }

    private fun hapus() {
        dtl_rw.visibility = View.VISIBLE
        val data = intent.getStringExtra("transaksi")
        transaksi = Gson().fromJson<Transaksi>(data,Transaksi::class.java)
        val id = transaksi.id
        ApiConfig.instanceRetrofit.cancel(id,1).enqueue(object : Callback<ResponModel> {
            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val respon = response.body()
                if (respon != null) {
                    if (respon.success == 1){
                        dtl_rw.visibility = View.GONE
                        onBackPressed()
                    }else{
                        Toast.makeText(this@DetailRiwayatActivity, "Error:" + respon.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                dtl_rw.visibility = View.GONE
                Toast.makeText(this@DetailRiwayatActivity, "Error:" + t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}