package com.example.mobilearek.adapter

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilearek.R
import com.example.mobilearek.activity.DetailMobilActivity
import com.example.mobilearek.model.Mobil
import com.example.mobilearek.util.Config
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterMobil(var activity: Activity, var data:ArrayList<Mobil>): RecyclerView.Adapter<AdapterMobil.Holder>() {

    class Holder(view: View):RecyclerView.ViewHolder(view){
        val tvNama = view.findViewById<TextView>(R.id.tv_nama)
        val tvHarga = view.findViewById<TextView>(R.id.tv_harga)
        val imgGambar= view.findViewById<ImageView>(R.id.img_mobil)
        val idMobil = view.findViewById<CardView>(R.id.id_mobil)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_mobil,parent,false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.tvNama.text = data[position].name
        holder.tvHarga.text = NumberFormat.getCurrencyInstance(Locale("in","ID")).format(Integer.valueOf(data[position].harga))
//        holder.imgGambar.setImageResource(data[position].image)
        val image = Config.urlData+ data[position].image
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.ic_baseline_arrow_back_24)
            .error(R.drawable.ic_baseline_close_24)
            .into(holder.imgGambar)

        holder.idMobil.setOnClickListener{
            val intent = Intent(activity,DetailMobilActivity::class.java)

            val str = Gson().toJson(data[position],Mobil::class.java)
            intent.putExtra("extra",str)
            activity.startActivity(intent)
        }
    }

}