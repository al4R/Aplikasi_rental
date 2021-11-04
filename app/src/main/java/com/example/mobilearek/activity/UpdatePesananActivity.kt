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
import kotlinx.android.synthetic.main.activity_update_pesanan.*
import kotlinx.android.synthetic.main.list_pesanan.*
import kotlinx.android.synthetic.main.toolbar.*
import java.text.SimpleDateFormat
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class UpdatePesananActivity : AppCompatActivity() {

    lateinit var myDb: MyDatabase
    lateinit var mobil: Mobil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_pesanan)

        myDb = MyDatabase.getInstance(this)!!
        getData()
        mainbutton()
    }
    private fun mainbutton(){
        btn_pilihSewa.setOnClickListener {
            setTglSewa()
        }
        btn_pilihAkhir.setOnClickListener {
            setTglKmb()
        }
        btn_update.setOnClickListener {
            if(sewa == null){
                toast("pilih tanggal sewa")
            }else if(kembali == null){
                toast("pilih tanggal kembali")
            }else {
                update()
            }
        }
    }

    val c:Calendar = Calendar.getInstance()
    var kembali : String? = null
    private fun setTglKmb() {
        DatePickerDialog(this, { _, yy, MM, dd ->
            var tglKmb = "$dd/${MM+1}/$yy"
            TimePickerDialog(this, { _, k, m ->
                tglKmb += " $k.$m"
                kembali = tglKmb
                Helper().ubahFormatTgl(tglKmb,up_kembali)
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
                    Helper().ubahFormatTgl(tglSewa,up_sewa)
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
    @SuppressLint("SimpleDateFormat")
    private fun cekHarga(){
        btn_update.isEnabled = true
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
            btn_update.isEnabled = false
            return
        } else if (tglSewa.time <= today.time) {
            toast("tanggal sewa tidak benar")
            btn_update.isEnabled = false
            return
        } else if (hari < 1 && jam < 1) {
            toast("minimal sewa 1 jam")
            btn_update.isEnabled = false
            return
        } else if (hari > 3) {
            toast("sewa terlalu lama")
            btn_update.isEnabled = false
            return
        } else if (tglSewa.time > besok) {
            toast("besok")
            btn_update.isEnabled = false
            return
        }else {
            val tmenit: Double = menit.toDouble() / 60 * hmobil
            val total = ((hari * 24) + jam) * hmobil + Math.round(tmenit)
            up_total.text = ("$total ")
            mobil.total = up_total.text.toString()
            mobil.tglSewa = up_sewa.text.toString()
            mobil.tglKembali = up_kembali.text.toString()
            mobil.lamaSewa = (hari * 24 + jam).toString()+" jam "+menit.toString()+" menit"
        }
    }
    fun toast(s: String){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }
    @SuppressLint("SetTextI18n")
    private fun getData() {
        val data = intent.getStringExtra("extra")
        mobil = Gson().fromJson<Mobil>(data, Mobil::class.java)
        up_nama.text = mobil.merk + " " + mobil.model
        up_harga.text = "Rp. " + mobil.harga + "/jam"
        up_kapasitas.text = mobil.kapasitas + " Orang"
        up_tahun.text = mobil.tahun
        up_transmisi.text = mobil.transmisi
        up_sewa.text = mobil.tglSewa
        up_kembali.text = mobil.tglKembali
        up_total.text = mobil.total
        sewa = mobil.TglS
        kembali = mobil.TglK
        up_sewa.text = mobil.tglSewa
        up_kembali.text = mobil.tglKembali
        val img = Config.urlData + mobil.image
        Picasso.get()
            .load(img)
            .placeholder(R.drawable.ic_baseline_directions_car_24_black)
            .error(R.drawable.ic_baseline_close_24)
            .resize(500,400)
            .into(up_gambar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Edit pesanan"
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    @SuppressLint("SimpleDateFormat")
    private fun update() {
        CompositeDisposable().add(Observable.fromCallable { myDb.daoPesan().update(mobil) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {

                Log.d("respons", "data updated")
            })
        val intent = Intent(this, PesananActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }


    override fun onResume() {
        super.onResume()
    }
}