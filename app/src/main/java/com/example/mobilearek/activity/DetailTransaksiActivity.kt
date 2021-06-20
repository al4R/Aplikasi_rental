package com.example.mobilearek.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.mobilearek.MainActivity
import com.example.mobilearek.R
import com.example.mobilearek.app.ApiConfig
import com.example.mobilearek.helper.SharedPref
import com.example.mobilearek.model.ResponModel
import com.example.mobilearek.model.Transaksi
import com.example.mobilearek.util.Config
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_pesanan.*
import kotlinx.android.synthetic.main.activity_detail_riwayat.*
import kotlinx.android.synthetic.main.activity_detail_transaksi.*
import kotlinx.android.synthetic.main.activity_detail_transaksi.tgl_pesan
import kotlinx.android.synthetic.main.activity_detail_transaksi.tgl_sewa
import kotlinx.android.synthetic.main.activity_detail_transaksi.totalByr
import kotlinx.android.synthetic.main.activity_detail_transaksi.tv_durasi
import kotlinx.android.synthetic.main.activity_profil_image.*
import kotlinx.android.synthetic.main.toolbar.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class DetailTransaksiActivity : AppCompatActivity() {

    lateinit var transaksi: Transaksi
    lateinit var s:SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_transaksi)

        s = SharedPref(this)
        mainButton()
        getData()

    }
    var fileImg: File? =null
    private fun mainButton() {
        bukti_byr.setOnClickListener {
            EasyImage.openGallery(this,1)
        }
        btn_upload_bukti.setOnClickListener {
            if(fileImg == null){
                Toast.makeText(this, "Pilih gambar", Toast.LENGTH_SHORT).show()
            }else{
                uploadImage()
            }

        }
        btn_batalT.setOnClickListener {
            cancel()
        }
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Detail Transaksi"
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
    private fun getData(){
        val user = s.getUser()
        val data = intent.getStringExtra("transaksi")
        transaksi = Gson().fromJson<Transaksi>(data,Transaksi::class.java)
        if (transaksi.status_bayar == 1){
            btn_upload_bukti.isEnabled = false
            btn_batalT.isEnabled = false
        }
        kode_trn.text = transaksi.kode_tran
        tgl_pesan.text = transaksi.tgl_order
        nama_pemesan.text = user!!.name
        tgl_sewa.text = transaksi.tgl_sewa
        tgl_kembali.text = transaksi.tgl_akhir_sewa
        tv_durasi.text = transaksi.lama_sewa
        totalByr.text = transaksi.total_harga
        val image= Config.urlBukti + transaksi.bukti_tf
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.ic_baseline_content_copy_24)
            .error(R.drawable.ic_baseline_close_24)
            .resize(400, 400)
            .into(bukti_byr)
    }
    private fun uploadImage() {
        loading_det_trans.visibility = View.VISIBLE
        val data = intent.getStringExtra("transaksi")
        transaksi = Gson().fromJson<Transaksi>(data,Transaksi::class.java)
        val id = transaksi.id
        val img = convertFile(fileImg!!)
        ApiConfig.instanceRetrofit.upload(id,img).enqueue(object : Callback<ResponModel> {
            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val respon = response.body()
                if(respon != null){
                    if (respon.success == 1){
                        loading_det_trans.visibility = View.GONE
                        Toast.makeText(this@DetailTransaksiActivity,"berhasil", Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    }else{
                        loading_det_trans.visibility = View.GONE
                        Toast.makeText(this@DetailTransaksiActivity, "Error : " + respon.message, Toast.LENGTH_SHORT).show()
                    }
                }else{
                    loading_det_trans.visibility = View.GONE
                    Toast.makeText(this@DetailTransaksiActivity, "Tidak ada respon" , Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                loading_det_trans.visibility = View.GONE
                Toast.makeText(this@DetailTransaksiActivity, "Error:" + t.message, Toast.LENGTH_SHORT).show()
            }

        })

    }
    fun convertFile(file: File): MultipartBody.Part{
        val reqFile: RequestBody = RequestBody.create(MediaType.parse("image/*"),file)
        return MultipartBody.Part.createFormData("bukti_tf",file.name,reqFile)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        EasyImage.handleActivityResult(requestCode,resultCode,data,this, object : DefaultCallback(){
            override fun onImagePicked(
                imageFile: File?, source: EasyImage.ImageSource?, type: Int) {
                fileImg = imageFile
                if (imageFile != null) {
                    Picasso.get()
                        .load(imageFile)
                        .placeholder(R.drawable.ic_baseline_arrow_back_24)
                        .error(R.drawable.ic_baseline_close_24)
                        .resize(400,400)
                        .into(bukti_byr)
                }
            }

        })
    }
    fun cancel(){
        loading_det_trans.visibility = View.VISIBLE
        val data = intent.getStringExtra("transaksi")
        transaksi = Gson().fromJson<Transaksi>(data,Transaksi::class.java)
        val id = transaksi.id
        ApiConfig.instanceRetrofit.cancel(id,1).enqueue(object : Callback<ResponModel> {
            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val respon = response.body()
                if (respon != null) {
                    if (respon.success == 1){
                        batalMobil()
                        loading_det_trans.visibility = View.GONE
                        onBackPressed()
                    }else{
                        loading_det_trans.visibility = View.GONE
                        Toast.makeText(this@DetailTransaksiActivity, "Error:" + respon.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                loading_det_trans.visibility = View.GONE
                Toast.makeText(this@DetailTransaksiActivity, "Error:" + t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
    fun batalMobil(){
        val data = intent.getStringExtra("transaksi")
        transaksi = Gson().fromJson<Transaksi>(data,Transaksi::class.java)
        val id = transaksi.details.mobil.id
        ApiConfig.instanceRetrofit.updatemobil(id,0).enqueue(object : Callback<ResponModel> {
            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val respon = response.body()
                if (respon != null) {
                    if (respon.success == 1){
                        Toast.makeText(this@DetailTransaksiActivity, "Berhasil ", Toast.LENGTH_SHORT).show()

                    }else{
                        Toast.makeText(this@DetailTransaksiActivity, "Error:" + respon.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                loading_det_trans.visibility = View.GONE
                Toast.makeText(this@DetailTransaksiActivity, "Error:" + t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}