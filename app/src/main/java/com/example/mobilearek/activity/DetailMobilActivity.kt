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
import java.text.SimpleDateFormat
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
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
            val data = myDb.daoPesan().getMobil(mobil.id)
            @Suppress("SENSELESS_COMPARISON")
            if (data == null){
                mobil.total = et_total.text.toString()
                mobil.tglSewa = et_tglSewa.text.toString()
                mobil.tglKembali = et_tglKmb.text.toString()
                mobil.jamSewa = et_jamSewa.text.toString()
                mobil.jamKembali = et_jamKmb.text.toString()
                insert()
            }else {
                mobil.total = et_total.text.toString()
                mobil.tglSewa = et_tglSewa.text.toString()
                mobil.tglKembali = et_tglKmb.text.toString()
                mobil.jamSewa = et_jamSewa.text.toString()
                mobil.jamKembali = et_jamKmb.text.toString()
                update()
            }
        }
        et_total.setOnClickListener {
            cekTgl()
        }
        et_tglSewa.setOnClickListener {
            openDatePicker()
        }
        et_jamSewa.setOnClickListener {
            openTimePicker()
        }
        et_tglKmb.setOnClickListener {
            openDatePicker2()
        }
        et_jamKmb.setOnClickListener {
            openTimePicker2()
        }
    }

    fun insert(){
        if(et_total.text.toString() == "0" ){
            toast("Periksa total bayar")
            return
        }else if (et_tglSewa.text.toString() == "Pilih tanggal") {
            toast("Tanggal Tidak boleh kosong")
            return
        } else if (et_jamSewa.text.toString() == "Pilih jam") {
            toast("Kolom jam tidak boleh kosng")
            return
        } else if (et_tglKmb.text.toString() == "Pilih tanggal") {
            toast("Tanggal Tidak boleh kosong")
            return
        } else if (et_jamKmb.text.toString() == "Pilih jam") {
            toast("Kolom jam tidak boleh kosng")
            return
        } else {
            getSewa()

            CompositeDisposable().add(Observable.fromCallable { myDb.daoPesan().insert(mobil) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {

                Log.d("respons", "data inserted")
            })
            val intent= Intent(this,PesananActivity::class.java)
            startActivity(intent)

        }
    }
    fun update(){
        CompositeDisposable().add(Observable.fromCallable { myDb.daoPesan().update(mobil) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {

                Log.d("respons", "data inserted")
            })
        val intent= Intent(this,PesananActivity::class.java)
        startActivity(intent)
    }


    @SuppressLint("SetTextI18n")
    fun getData(){
        val data = intent.getStringExtra("extra")
        mobil = Gson().fromJson<Mobil>(data, Mobil::class.java)


        if(mobil.tglSewa == null){
            et_tglSewa.text = "Pilih tanggal"
        }else{
            et_tglSewa.text = mobil.tglSewa
        }
        if (mobil.jamSewa == null){
            et_jamSewa.text = "Pilih jam"
        }else{
            et_jamSewa.text = mobil.jamSewa
        }
        if(mobil.tglKembali==null){
            et_tglKmb.text = "Pilih tanggal"
        }else{
            et_tglKmb.text = mobil.tglKembali
        }
        if(mobil.jamKembali==null){
            et_jamKmb.text = "Pilih jam"
        }else{
            et_jamKmb.text = mobil.jamKembali
        }
        if(mobil.total==null){
            et_total.text = "0"
        }else{
            et_total.text = mobil.total
        }
        tv_nama.text = mobil.merk +" "+ mobil.name
        tv_harga.text = "Rp. "+mobil.harga+"/jam"
//            NumberFormat.getCurrencyInstance(Locale("in","ID")).format(Integer.valueOf(mobil.harga))
        tv_kapasitas.text = mobil.kapasitas+" Orang"
        tv_tahun.text = mobil.tahun
        tv_transmisi.text = mobil.transmisi
//        tv_deskripsi.text = mobil.deskripsi


        val img = Config.urlData + mobil.image
        Picasso.get()
            .load(img)
            .placeholder(R.drawable.ic_baseline_arrow_back_24)
            .error(R.drawable.ic_baseline_close_24)
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

    val c:Calendar = Calendar.getInstance()


    @SuppressLint("SimpleDateFormat")
    fun openDatePicker() {
        DatePickerDialog(this, { _, yy, MM, dd ->
            val dt = "$dd ${MM+1} $yy"
            val formatLama = "d MM yyyy"
            val formatBaru = "dd MMMM yyyy"
            val dateFormat = SimpleDateFormat(formatLama)
            val convert = dateFormat.parse(dt)
            dateFormat.applyPattern(formatBaru)
            val newFormat = dateFormat.format(convert)
            et_tglSewa.setText(newFormat)
        },c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    @SuppressLint("SimpleDateFormat")
    fun openTimePicker() {
        TimePickerDialog(this, { _, k, m ->
            val tm = "$k.$m"
            if(k <= 5){
                toast("Harus lebih dari jam 5")
                return@TimePickerDialog
            }else if(k >= 17) {
                toast("Harus kurang dari jam 16")
                return@TimePickerDialog
            }else{
                val formatLama = "k.m"
                val formatBaru = "kk.mm"
                val dateFormat = SimpleDateFormat(formatLama)
                val convert = dateFormat.parse(tm)
                dateFormat.applyPattern(formatBaru)
                dateFormat.applyPattern(formatBaru)
                val newFormat = dateFormat.format(convert)
                et_jamSewa.setText(newFormat)
            }
        },c.get(Calendar.HOUR),c.get(Calendar.MINUTE),true
        ).show()
    }

    @SuppressLint("SimpleDateFormat")
    fun openTimePicker2() {
        TimePickerDialog(this, { _, k, m ->
            val tm = "$k.$m"
            val formatLama = "k.m"
            val formatBaru = "kk.mm"
            val dateFormat = SimpleDateFormat(formatLama)
            val convert = dateFormat.parse(tm)
            dateFormat.applyPattern(formatBaru)
            dateFormat.applyPattern(formatBaru)
            val newFormat = dateFormat.format(convert)

            et_jamKmb.setText(newFormat)
        },c.get(Calendar.HOUR),c.get(Calendar.MINUTE),true
        ).show()

    }

    @SuppressLint("SimpleDateFormat")
    fun openDatePicker2() {
        DatePickerDialog(this, { _, yy, MM, dd ->
            val dt = "$dd ${MM+1} $yy"
            val formatLama = "d MM yyyy"
            val formatBaru = "dd MMMM yyyy"
            val dateFormat = SimpleDateFormat(formatLama)
            val convert = dateFormat.parse(dt)
            dateFormat.applyPattern(formatBaru)
            val newFormat = dateFormat.format(convert)

            et_tglKmb.setText(newFormat)
        },c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    fun toast(s: String){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    fun cekTgl(){
        if (et_tglSewa.text.toString() == "Pilih tanggal") {
            toast("Tanggal Tidak boleh kosong")
            return
        } else if (et_jamSewa.text.toString() == "Pilih jam") {
            toast("Kolom jam tidak boleh kosng")
            return
        } else if (et_tglKmb.text.toString() == "Pilih tanggal") {
            toast("Tanggal Tidak boleh kosong")
            return
        } else if (et_jamKmb.text.toString() == "Pilih jam") {
            toast("Kolom jam tidak boleh kosng")
            return
        }else {
            getSewa()
        }
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    fun getSewa(){
        var sewa = et_tglSewa.text.toString() + et_jamSewa.text.toString()
        var kembali = et_tglKmb.text.toString() + et_jamKmb.text.toString()
        val today = Date()
        val sdf = SimpleDateFormat("dd MMMM yyyykk.mm")
        val convert = sdf.parse(sewa)
        val convert2 = sdf.parse(kembali)
        sdf.applyPattern("dd MM yyyy kk.mm")
        val tgl_sewa = sdf.format(convert)
        val tgl_kmb = sdf.format(convert2)
        val tglSewa: Date = sdf.parse(tgl_sewa)
        val tglKmb: Date = sdf.parse(tgl_kmb)
        val mobil = mobil.harga.toInt()
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
            val tmenit: Double = menit.toDouble() / 60 * mobil
           val total = ((hari * 24) + jam) * mobil + Math.round(tmenit)
           et_total.setText("$total ")

        }
    }

}