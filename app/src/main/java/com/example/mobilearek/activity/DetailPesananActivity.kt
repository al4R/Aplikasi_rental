package com.example.mobilearek.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.mobilearek.R
import com.example.mobilearek.model.Mobil
import com.example.mobilearek.util.Config
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_pesanan.*
import kotlinx.android.synthetic.main.list_pesanan.tv_mobil

class DetailPesananActivity : AppCompatActivity() {
    lateinit var mobil: Mobil
    lateinit var sp: Spinner
    lateinit var tv_spiner: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_pesanan)


        init()
        spiner()
        mainButton()
    }
    fun mainButton(){
//        tv_tglPesan.setOnClickListener {
//            setTglSewa()
//        }
//        tv_jamPesan.setOnClickListener {
//            setJamSewa()
//        }
//        tv_tglKembali.setOnClickListener {
//            setTglKmb()
//        }
//        tv_jamKembali.setOnClickListener {
//            setJamKmb()
//        }
//        tv_totalBayar.setOnClickListener {
//            getBayar()
//        }
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
//                tv_spiner.text = option.get(position)
                if(position == 0){
                    tv_noRek.visibility = View.GONE
                    tv_spiner.text = "Pilih metode bayar"
                }else if(position == 1){
                    tv_spiner.text = "Dana"
                    tv_noRek.visibility = View.GONE
                    iv_bayar.setImageResource(R.drawable.dana)
                }else if (position == 2){
                    tv_spiner.text = "ovo"
                    tv_noRek.visibility = View.GONE
                }else if (position == 3){
                    tv_noRek.visibility = View.VISIBLE
                    tv_spiner.text = "bca"
                }else if (position == 4){
                    tv_noRek.visibility = View.VISIBLE
                    tv_spiner.text = "bri"
                }else if (position == 5){
                    tv_noRek.visibility = View.VISIBLE
                    tv_spiner.text = "mandiri"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }
    private fun init() {
        sp = findViewById(R.id.spiner) as Spinner
        tv_spiner = findViewById(R.id.tv_spiner) as TextView
    }
    fun toast(s: String){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }
    @SuppressLint("SetTextI18n")
    fun getData(){
        val data = intent.getStringExtra("extra")
        mobil = Gson().fromJson<Mobil>(data, Mobil::class.java)

        tv_mobil.text = mobil.merk+" "+mobil.name
        tv_trans.text = mobil.transmisi
        tv_noPol.text = mobil.no_kendaraan
        tv_tglPesan.text = mobil.tglSewa
        tv_jamPesan.text = mobil.jamSewa
        tv_tglKembali.text = mobil.tglKembali
        tv_jamKembali.text = mobil.jamKembali
        tv_totalBayar.text = mobil.total
        val img = Config.urlData + mobil.image
        Picasso.get()
            .load(img)
            .placeholder(R.drawable.ic_baseline_arrow_back_24)
            .error(R.drawable.ic_baseline_close_24)
            .into(iv_mobil)
    }

    override fun onResume() {
        super.onResume()
        getData()
    }
}
