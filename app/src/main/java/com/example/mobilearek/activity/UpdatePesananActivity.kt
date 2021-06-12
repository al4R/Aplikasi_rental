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
    fun mainbutton(){
    up_total.setOnClickListener {
        getSewa()
    }
    up_tgl_sewa.setOnClickListener {
        openDatePicker()
    }
    up_tgl_kmb.setOnClickListener {
        openDatePicker2()
    }
    up_jam_sewa.setOnClickListener {
        openTimePicker()
    }
    up_jam_kmb.setOnClickListener {
        openTimePicker2()
    }
    btn_update.setOnClickListener {
        update()
    }
    }


    val c:Calendar = Calendar.getInstance()
    private fun openTimePicker() {
        TimePickerDialog(this, { _, k, m ->
            if(k <= 5){
                toast("Harus lebih dari jam 5")
                return@TimePickerDialog
            }else if(k >= 17) {
                toast("Harus kurang dari jam 16")
                return@TimePickerDialog
            }else{
                Helper().ubahFormatJam("$k.$m",up_jam_sewa)
            }
        },c.get(Calendar.HOUR),c.get(Calendar.MINUTE),true
        ).show()
    }
    private fun openTimePicker2() {
        TimePickerDialog(this, { _, k, m ->
            Helper().ubahFormatJam("$k.$m",up_jam_kmb)
        },c.get(Calendar.HOUR),c.get(Calendar.MINUTE),true
        ).show()
    }
    private fun openDatePicker() {
        DatePickerDialog(this, { _, yy, MM, dd ->
            Helper().ubahFormatTgl("$dd ${MM+1} $yy",up_tgl_sewa)
        },c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
    private fun openDatePicker2() {
        DatePickerDialog(this, { _, yy, MM, dd ->
            Helper().ubahFormatTgl("$dd ${MM+1} $yy",up_tgl_kmb)
        },c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
    fun toast(s: String){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }
    @SuppressLint("SetTextI18n")
    fun getData() {
        val data = intent.getStringExtra("extra")
        mobil = Gson().fromJson<Mobil>(data, Mobil::class.java)
        up_nama.text = mobil.merk + " " + mobil.name
        up_harga.text = "Rp. " + mobil.harga + "/jam"
        up_kapasitas.text = mobil.kapasitas + " Orang"
        up_tahun.text = mobil.tahun
        up_transmisi.text = mobil.transmisi
        up_tgl_sewa.text = mobil.tglSewa
        up_tgl_kmb.text = mobil.tglKembali
        up_jam_sewa.text = mobil.jamSewa
        up_jam_kmb.text = mobil.jamKembali
        up_total.text = mobil.total

        val img = Config.urlData + mobil.image
        Picasso.get()
            .load(img)
            .placeholder(R.drawable.ic_baseline_arrow_back_24)
            .error(R.drawable.ic_baseline_close_24)
            .into(up_gambar)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Update Pesanan"
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
    @SuppressLint("SimpleDateFormat")
    fun update() {
        getSewa()
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
    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    @SuppressLint("SimpleDateFormat")
    fun getSewa(){
        val sewa = up_tgl_sewa.text.toString() + up_jam_sewa.text.toString()
        val kembali = up_tgl_kmb.text.toString() + up_jam_kmb.text.toString()
        val today = Date()
        val sdf = SimpleDateFormat("dd MMMM yyyykk.mm")
        val convert = sdf.parse(sewa)
        val convert2 = sdf.parse(kembali)
        sdf.applyPattern("dd MM yyyy kk.mm")
        val tsewa = sdf.format(convert)
        val tkmb = sdf.format(convert2)
        val tglSewa: Date = sdf.parse(tsewa)
        val tglKmb: Date = sdf.parse(tkmb)
        val hmobil = mobil.harga.toInt()
        val hari: Long = (tglKmb.time - tglSewa.time) / 86400000
        val jam: Long = (tglKmb.time - tglSewa.time) % 86400000 / 3600000
        val menit: Long = (tglKmb.time - tglSewa.time) % 86400000 % 3600000 / 60000
        if (tglSewa.time > tglKmb.time) {
            toast("input tidak benar")
            return
        } else if (tglSewa.time <= today.time) {
            toast("tanggal atau jam sewa salah")
            return
        } else if (hari < 1 && jam < 1) {
            toast("minimal sewa 1 jam")
            return
        } else if (hari > 7) {
            toast("sewa terlalu lama")
            return
        } else {
            val tmenit: Double = menit.toDouble() / 60 * hmobil
            val total = ((hari * 24) + jam) * hmobil + Math.round(tmenit)
            up_total.text = ("$total ")
            mobil.total = up_total.text.toString()
            mobil.tglSewa = up_tgl_sewa.text.toString()
            mobil.tglKembali = up_tgl_kmb.text.toString()
            mobil.jamSewa = up_jam_sewa.text.toString()
            mobil.jamKembali =up_jam_kmb.text.toString()
        }
    }
}