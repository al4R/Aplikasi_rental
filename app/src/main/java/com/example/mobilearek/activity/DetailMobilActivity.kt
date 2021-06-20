package com.example.mobilearek.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.mobilearek.R
import com.example.mobilearek.helper.Helper
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
import java.text.SimpleDateFormat
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DetailMobilActivity : AppCompatActivity() {
    lateinit var myDb: MyDatabase
    lateinit var mobil: Mobil
    lateinit var etTotal : TextView
    lateinit var etTglSewa : TextView
    lateinit var etJamSewa : TextView
    lateinit var etTglKmb : TextView
    lateinit var etJamKmb : TextView
    lateinit var s:SharedPref



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_mobil)
        myDb = MyDatabase.getInstance(this)!!

        init()
        getData()
        if (s.getStatusLogin() == false){
            btn_pesan.isEnabled = false
        }else if(mobil.status == 1){
            btn_pesan.isEnabled = false
        }
        etTotal.setOnClickListener {
            cek()
        }
        etTglSewa.setOnClickListener {
            openDatePicker()
        }
        etJamSewa.setOnClickListener {
            openTimePicker()
        }
        etTglKmb.setOnClickListener {
            openDatePicker2()
        }
        etJamKmb.setOnClickListener {
            openTimePicker2()
        }

        btn_pesan.setOnClickListener{
            val data = myDb.daoPesan().getMobil(mobil.id)
            @Suppress("SENSELESS_COMPARISON")
            if (data == null){
                getSewa()
            }else {
                toast("Periksa pesanan")
            }
        }
    }
    var ts:Long? = null
    var tk:Long? = null
    var now:Long?= null

    fun init(){
        etTotal = findViewById(R.id.et_total)
        etTglSewa = findViewById(R.id.et_tgl_sewa)
        etJamSewa = findViewById(R.id.et_jam_sewa)
        etTglKmb = findViewById(R.id.et_tgl_kmb)
        etJamKmb = findViewById(R.id.et_jam_kmb)
        s= SharedPref(this)
    }
    fun insert(){
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
    @SuppressLint("SetTextI18n")
    fun getData(){
        val data = intent.getStringExtra("extra")
        mobil = Gson().fromJson<Mobil>(data, Mobil::class.java)
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
        return true
    }

    val c:Calendar = Calendar.getInstance()


    @SuppressLint("SimpleDateFormat")
    fun openDatePicker() {
        DatePickerDialog(this, { _, yy, MM, dd ->
            Helper().ubahFormatTgl("$dd ${MM+1} $yy",etTglSewa)
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
                Helper().ubahFormatJam(tm,etJamSewa)
            }
        },c.get(Calendar.HOUR),c.get(Calendar.MINUTE),true
        ).show()
    }

    @SuppressLint("SimpleDateFormat")
    fun openTimePicker2() {
        TimePickerDialog(this, { _, k, m ->
            val tm = "$k.$m"
            Helper().ubahFormatJam(tm,etJamKmb)
        },c.get(Calendar.HOUR),c.get(Calendar.MINUTE),true
        ).show()

    }

    @SuppressLint("SimpleDateFormat")
    fun openDatePicker2() {
        DatePickerDialog(this, { _, yy, MM, dd ->
            val dt = "$dd ${MM+1} $yy"
            Helper().ubahFormatTgl(dt,etTglKmb)
        },c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    fun toast(s: String){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    fun getSewa(){
        if (etTglSewa.text.toString() == "Pilih tanggal") {
            toast("Tanggal Tidak boleh kosong")
            return
        } else if (etJamSewa.text.toString() == "Pilih jam") {
            toast("Kolom jam tidak boleh kosng")
            return
        } else if (etTglKmb.text.toString() == "Pilih tanggal") {
            toast("Tanggal Tidak boleh kosong")
            return
        } else {
            val sewa = etTglSewa.text.toString() + etJamSewa.text.toString()
            val kembali = etTglKmb.text.toString() + etJamKmb.text.toString()
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
            ts = tglSewa.time
            tk = tglKmb.time
            now = today.time
            if (etJamKmb.text.toString() == "Pilih jam") {
                toast("Kolom jam tidak boleh kosng")
                return
            } else if (tglSewa.time > tglKmb.time) {
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
                etTotal.text = ("$total ")
                mobil.total = etTotal.text.toString()
                mobil.tglSewa = etTglSewa.text.toString()
                mobil.tglKembali = etTglKmb.text.toString()
                mobil.jamSewa = etJamSewa.text.toString()
                mobil.jamKembali = etJamKmb.text.toString()
                insert()
            }
        }
    }
    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    fun cek(){
        if (etTglSewa.text.toString() == "Pilih tanggal") {
            toast("Tanggal Tidak boleh kosong")
            return
        } else if (etJamSewa.text.toString() == "Pilih jam") {
            toast("Kolom jam tidak boleh kosng")
            return
        } else if (etTglKmb.text.toString() == "Pilih tanggal") {
            toast("Tanggal Tidak boleh kosong")
            return
        } else if (etJamKmb.text.toString() == "Pilih jam") {
            toast("Kolom jam tidak boleh kosng")
            return
        }else {
            val sewa = etTglSewa.text.toString() + etJamSewa.text.toString()
            val kembali = etTglKmb.text.toString() + etJamKmb.text.toString()
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
            ts = tglSewa.time
            tk = tglKmb.time
            now = today.time
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
                etTotal.text = ("$total ")
                mobil.total = etTotal.text.toString()
                mobil.tglSewa = etTglSewa.text.toString()
                mobil.tglKembali = etTglKmb.text.toString()
                mobil.jamSewa = etJamSewa.text.toString()
                mobil.jamKembali = etJamKmb.text.toString()
            }
        }
    }

}