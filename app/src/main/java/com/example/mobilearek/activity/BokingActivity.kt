package com.example.mobilearek.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.mobilearek.R
import com.example.mobilearek.helper.Helper
import com.example.mobilearek.model.Mobil
import com.example.mobilearek.room.MyDatabase
import com.example.mobilearek.util.Config
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_boking.*
import kotlinx.android.synthetic.main.toolbar.*
import java.text.SimpleDateFormat
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class BokingActivity : AppCompatActivity() {

    lateinit var myDb: MyDatabase
    lateinit var mobil: Mobil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boking)

        myDb = MyDatabase.getInstance(this)!!
        getData()
        mainButton()


    }
    @SuppressLint("SetTextI18n")
    private fun getData(){
        val data = intent.getStringExtra("extra")
        mobil = Gson().fromJson<Mobil>(data, Mobil::class.java)
        bk_mobil.text = mobil.merk+" "+mobil.model
        bk_harga.text = "Rp. "+mobil.harga+"/jam"
//            NumberFormat.getCurrencyInstance(Locale("in","ID")).format(Integer.valueOf(mobil.harga))
        bk_kapasitas.text = mobil.kapasitas+" Orang"
        bk_tahun.text = mobil.tahun
        bk_transmisi.text = mobil.transmisi

//        tv_deskripsi.text = mobil.deskripsi


        val img = Config.urlData + mobil.image
        Picasso.get()
            .load(img)
            .placeholder(R.drawable.ic_baseline_arrow_back_24)
            .error(R.drawable.ic_baseline_close_24)
            .into(bk_gambar)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = mobil.merk+" "+mobil.model
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    private fun mainButton() {
       bk_pilihSewa.setOnClickListener {
           setTglSewa()
       }
       bk_pilihAkhir.setOnClickListener {
           setTglKmb()
       }
       btn_boking.setOnClickListener {
           insert()
       }
    }
    private fun insert(){
        CompositeDisposable().add(Observable.fromCallable { myDb.daoPesan().insert(mobil) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {

                Log.d("respons", "data inserted")
            })
        val intent= Intent(this,PesananActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()

    }
    val c: Calendar = Calendar.getInstance()

    var kembali : String? = null
    private fun setTglKmb() {
        DatePickerDialog(this, { _, yy, MM, dd ->
            var tglKmb = "$dd/${MM+1}/$yy"
            TimePickerDialog(this, { _, k, m ->
                tglKmb += " $k.$m"
                kembali = tglKmb
                Helper().ubahFormatTgl(tglKmb,bk_kembali)
                if (sewa != null && kembali != null)
                {
                    cekHarga()
                }
            },c.get(Calendar.HOUR),c.get(Calendar.MINUTE),true
            ).show()
        },c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    var sewa : String? = null
    private fun setTglSewa() {
        DatePickerDialog(this, { _, yy, MM, dd ->
            var tglSewa = "$dd/${MM+1}/$yy"
            TimePickerDialog(this, { _, k, m ->
                tglSewa += " $k.$m"
                if(k <= 6){
                    toast("Harus lebih dari jam 6")
                    return@TimePickerDialog
                }else if(k >= 17) {
                    toast("Harus kurang dari jam 16")
                    return@TimePickerDialog
                }else{
                    sewa = tglSewa
                    Helper().ubahFormatTgl(tglSewa,bk_sewa)
                    if (sewa != null && kembali != null)
                    {
                        cekHarga()
                    }
                }
            },c.get(Calendar.HOUR),c.get(Calendar.MINUTE),true
            ).show()
        },c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    fun toast(s: String){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }


    @SuppressLint("SimpleDateFormat")
    private fun cekHarga(){
        btn_boking.isEnabled = true
            val today = Date()
            val sdf = SimpleDateFormat("dd/MM/yyyy HH.mm")
            val tglSewa: Date = sdf.parse(sewa)
            val tglKmb: Date = sdf.parse(kembali)
            val hmobil = mobil.harga.toInt()
            val hari: Long = (tglKmb.time - tglSewa.time) / 86400000
            val jam: Long = (tglKmb.time - tglSewa.time) % 86400000 / 3600000
            val menit: Long = (tglKmb.time - tglSewa.time) % 86400000 % 3600000 / 60000
            val besok = today.time + 86400000
            if (tglSewa.time > tglKmb.time) {
                toast("input tidak benar")
                btn_boking.isEnabled = false
                return
            } else if (tglSewa.time <= today.time) {
                toast("tanggal sewa tidak benar")
                btn_boking.isEnabled = false
                return
            } else if (hari < 1 && jam < 1) {
                toast("minimal sewa 1 jam")
                btn_boking.isEnabled = false
                return
            } else if (hari > 3) {
                toast("sewa terlalu lama")
                btn_boking.isEnabled = false
                return
            } else if (tglSewa.time > besok) {
                toast("besok")
                btn_boking.isEnabled = false
                return
            }else {
                val tmenit: Double = menit.toDouble() / 60 * hmobil
                val total = ((hari * 24) + jam) * hmobil + Math.round(tmenit)
                bk_total.text = ("$total ")
                mobil.total = bk_total.text.toString()
                mobil.tglSewa = bk_sewa.text.toString()
                mobil.tglKembali = bk_kembali.text.toString()
                mobil.TglS = sewa
                mobil.TglK = kembali
                mobil.lamaSewa = (hari * 24 + jam).toString()+" jam "+menit.toString()+" menit"

            }
        }
    @SuppressLint("SimpleDateFormat")
    private fun boking(){
//            val today = Date()
//            val sdf = SimpleDateFormat("dd/MM/yyyy HH.mm")
//            val tglSewa: Date = sdf.parse(sewa)
//            val tglKmb: Date = sdf.parse(kembali)
//            val hmobil = mobil.harga.toInt()
//            val hari: Long = (tglKmb.time - tglSewa.time) / 86400000
//            val jam: Long = (tglKmb.time - tglSewa.time) % 86400000 / 3600000
//            val menit: Long = (tglKmb.time - tglSewa.time) % 86400000 % 3600000 / 60000
//            val besok = today.time + 86400000
//            if (tglSewa.time > tglKmb.time) {
//                toast("input tidak benar")
//                return
//            } else if (tglSewa.time <= today.time) {
//                toast("tanggal atau jam sewa salah")
//                return
//            } else if (hari < 1 && jam < 1) {
//                toast("minimal sewa 1 jam")
//                return
//            } else if (hari > 7) {
//                toast("sewa terlalu lama")
//                return
//            } else if (tglSewa.time > besok) {
//                toast("besok")
//                return
//            }else {
//                val tmenit: Double = menit.toDouble() / 60 * hmobil
//                val total = ((hari * 24) + jam) * hmobil + Math.round(tmenit)
//                bk_total.text = ("$total ")
//                mobil.total = bk_total.text.toString()
//                mobil.tglSewa = bk_sewa.text.toString()
//                mobil.tglKembali = bk_kembali.text.toString()
//                mobil.lamaSewa = (hari * 24 + jam).toString()+" jam "+menit.toString()+" menit"
//                insert()
//            }
        }

    override fun onResume() {
        super.onResume()
    }
}