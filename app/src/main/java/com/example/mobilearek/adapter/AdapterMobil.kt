package com.example.mobilearek.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilearek.R
import com.example.mobilearek.activity.DetailMobilActivity
import com.example.mobilearek.model.Mobil
import com.example.mobilearek.model.Page
import com.example.mobilearek.util.Config
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
class AdapterMobil(var activity: Activity): RecyclerView.Adapter<AdapterMobil.Holder>(), Filterable {
    var data  = ArrayList<Mobil>();
    var datafilter = ArrayList<Mobil>();

    fun setData(){
        this.data = data;
        this.datafilter = data;
        notifyDataSetChanged()
    }

    class Holder(view: View):RecyclerView.ViewHolder(view){
        val tvNama = view.findViewById<TextView>(R.id.tv_nama)
        val tvHarga = view.findViewById<TextView>(R.id.tv_harga)
        val imgGambar= view.findViewById<ImageView>(R.id.img_mobil)
        val idMobil = view.findViewById<CardView>(R.id.id_mobil)
        val tvStatus = view.findViewById<TextView>(R.id.tv_status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_mobil,parent,false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.tvNama.text = data[position].merk +" "+ data[position].model
        holder.tvHarga.text = NumberFormat.getCurrencyInstance(Locale("in","ID")).format(Integer.valueOf(data[position].harga))
//        holder.imgGambar.setImageResource(data[position].image)
        val status = data[position].status
        if(status == 2){
            holder.tvStatus.text="Diboking"
            holder.tvStatus.setTextColor(activity.getResources().getColor(R.color.Kuning))
        }else if(status == 1){
            holder.tvStatus.text="Dipinjam"
            holder.tvStatus.setTextColor(activity.getResources().getColor(R.color.ColorRed))
        }else if(status == 0){
            holder.tvStatus.text="Tersedia"
        }
        val image = Config.urlData+ data[position].image
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.ic_baseline_directions_car_24)
            .error(R.drawable.ic_baseline_close_24)
            .resize(400,400)
            .into(holder.imgGambar)

        holder.idMobil.setOnClickListener{
            val intent = Intent(activity, DetailMobilActivity::class.java)
            val str = Gson().toJson(data[position],Mobil::class.java)
            intent.putExtra("extra",str)
            activity.startActivity(intent)
        }
    }

    fun clear(){
        data.clear()
        notifyDataSetChanged()
    }
    fun addList(items: ArrayList<Mobil>){
        data.addAll(items)
        notifyDataSetChanged()
    }
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterR = FilterResults()

                if(constraint == null || constraint.length < 0){
                    filterR.count = datafilter.size
                    filterR.values = datafilter
                }else{
                    var searchChr = constraint.toString()

                    val itemM = ArrayList<Mobil>()

                    for (item in datafilter){
                        val mobil = item.merk+" "+item.model
                        if(mobil.toLowerCase().contains(searchChr) ){
                            itemM.add(item)
                        }
                    }
                    filterR.count = itemM.size
                    filterR.values = itemM
                }
                return filterR
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                data = results!!.values as ArrayList<Mobil>
                notifyDataSetChanged()
            }
        }
    }

}