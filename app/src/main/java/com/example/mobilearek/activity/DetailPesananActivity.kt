package com.example.mobilearek.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.mobilearek.MainActivity
import com.example.mobilearek.R
import com.example.mobilearek.app.ApiConfig
import com.example.mobilearek.helper.SharedPref
import com.example.mobilearek.model.Mobil
import com.example.mobilearek.model.ResponModel
import com.example.mobilearek.model.Pesanan
import com.example.mobilearek.room.MyDatabase
import com.example.mobilearek.util.Config
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail_pesanan.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.list_pesanan.tv_mobil
import kotlinx.android.synthetic.main.toolbar.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

class DetailPesananActivity : AppCompatActivity() {
    lateinit var mobil: Mobil
    lateinit var sp: Spinner
    lateinit var tvSpiner: TextView
    lateinit var ivBayar: ImageView
    lateinit var myDb:MyDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_pesanan)
        myDb = MyDatabase.getInstance(this)!!

        init()
        spiner()
        btn_pesan.setOnClickListener {
            if (tvSpiner.text == "Pilih metode bayar"){
                toast("Pilih metode pembayaran")
            }else{
                progres_bar.visibility = View.VISIBLE
                sewa()
            }

        }

    }

    private fun sewa() {
        val user = SharedPref(this).getUser()
        val data = intent.getStringExtra("extra")
        mobil = Gson().fromJson<Mobil>(data, Mobil::class.java)
        val sewa = Pesanan()
        sewa.user_id = ""+user!!.id
        sewa.tgl_order = "now"
        sewa.total_harga = ""+mobil.total
        sewa.tgl_sewa=""+mobil.tglSewa
        sewa.tgl_akhir_sewa=""+mobil.tglKembali
        sewa.transfer = ""+tvSpiner.text.toString()
        sewa.lama_sewa=""+mobil.lamaSewa
        sewa.mobil_id =""+mobil.id
        sewa.harga_sewa=""+mobil.harga


        ApiConfig.instanceRetrofit.pesan(sewa).enqueue(object : retrofit2.Callback<ResponModel> {
            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {

                val respon = response.body()
                if (respon != null) {
                    if (respon.success == 1){
                        updateMobil()
                    }else{
                        progres_bar.visibility = View.GONE
                        Toast.makeText(this@DetailPesananActivity, "Error:" + respon.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                progres_bar.visibility = View.GONE
                Toast.makeText(this@DetailPesananActivity, "Error:" + t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun updateMobil(){
        val id = mobil.id
        ApiConfig.instanceRetrofit.updatemobil(id,2).enqueue(object : retrofit2.Callback<ResponModel> {
            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val respon = response.body()
                if (respon != null) {
                    if (respon.success == 1){
                        progres_bar.visibility = View.GONE
                        delete()
                        val intent = Intent (this@DetailPesananActivity, MainActivity::class.java)
                        Toast.makeText(this@DetailPesananActivity, "Berhasil memesan ", Toast.LENGTH_SHORT).show()
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }else{
                        progres_bar.visibility = View.GONE
                        Toast.makeText(this@DetailPesananActivity, "Error:" + respon.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                progres_bar.visibility = View.GONE
                Toast.makeText(this@DetailPesananActivity, "Error:" + t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun delete(){
        CompositeDisposable().add(Observable.fromCallable { myDb.daoPesan().delete(mobil) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {

                Log.d("respons", "data deleted")
            })
    }
    fun spiner(){
        val option = arrayOf("Pilih metode bayar","Dana","OVO","Bank BCA","Bank BRI","Bank Mandiri")

        sp.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,option)

        sp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(position == 0){
                    ivBayar.setImageResource(R.color.colorWhite)
                    tv_noRek.visibility = View.GONE
                    tvSpiner.text = "Pilih metode bayar"
                }else if(position == 1){
                    tvSpiner.text = "085747488316"
                    tv_noRek.visibility = View.GONE
                    ivBayar.setImageResource(R.drawable.dana)
                }else if (position == 2){
                    tvSpiner.text = "085789349864"
                    Picasso.get()
                        .load(R.drawable.ovo)
                        .placeholder(R.drawable.ic_baseline_arrow_back_24)
                        .error(R.drawable.ic_baseline_close_24)
                        .resize(400,200)
                        .into(ivBayar)
                    tv_noRek.visibility = View.GONE
                }else if (position == 3){
                    tv_noRek.visibility = View.VISIBLE
                    tvSpiner.text = "1234xxxxxxx7777"
                    Picasso.get()
                        .load(R.drawable.bca)
                        .placeholder(R.drawable.ic_baseline_arrow_back_24)
                        .error(R.drawable.ic_baseline_close_24)
                        .resize(400,200)
                        .into(ivBayar)
                }else if (position == 4){
                    tv_noRek.visibility = View.VISIBLE
                    tvSpiner.text = "1234xxxxxxx8888"
                    ivBayar.setImageResource(R.drawable.bri)
                }else if (position == 5){
                    tv_noRek.visibility = View.VISIBLE
                    tvSpiner.text = "1234xxxxxxx9999"
                    ivBayar.setImageResource(R.drawable.mandiri)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }
    private fun init() {
        sp = findViewById(R.id.spiner)
        tvSpiner = findViewById(R.id.tv_spiner)
        ivBayar = findViewById(R.id.iv_bayar)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Detail Pesanan"
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
    fun toast(s: String){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }
    @SuppressLint("SetTextI18n")
    fun getData(){
        val data = intent.getStringExtra("extra")
        mobil = Gson().fromJson<Mobil>(data, Mobil::class.java)
        tv_mobil.text = mobil.merk+" "+mobil.model
        tv_trans.text = mobil.transmisi
        tv_noPol.text = mobil.no_kendaraan
        tv_tglPesan.text = mobil.tglSewa
        tv_tglKembali.text = mobil.tglKembali
        tv_totalBayar.text = mobil.total
        val img = Config.urlData + mobil.image
        Picasso.get()
            .load(img)
            .placeholder(R.drawable.ic_baseline_arrow_back_24)
            .error(R.drawable.ic_baseline_close_24)
            .into(iv_mobil)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
    override fun onResume() {
        getData()
        super.onResume()

    }
}
