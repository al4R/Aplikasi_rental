package com.example.mobilearek.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilearek.R
import com.example.mobilearek.activity.DetailMobilActivity
import com.example.mobilearek.activity.DetailRiwayatActivity
import com.example.mobilearek.helper.SharedPref
import com.example.mobilearek.model.Mobil
import com.example.mobilearek.model.Transaksi
import com.google.gson.Gson


class AdapterRiwayat( var data : ArrayList<Transaksi>,var listener: Listeners):RecyclerView.Adapter<AdapterRiwayat.Holder>() {
    class Holder(view: View):RecyclerView.ViewHolder(view){

        val tvKode = view.findViewById<TextView>(R.id.rw_kode)
        val tvtglPesan = view.findViewById<TextView>(R.id.tanggal_pesan)
        val tvTotal = view.findViewById<TextView>(R.id.rw_total)
        val cdRiwayat = view.findViewById<CardView>(R.id.riwayat)
        val lanjut = view.findViewById<TextView>(R.id.rw_lanjut)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_riwayat,parent,false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = data[position]
        holder.tvtglPesan.text = data.tgl_order
        holder.tvKode.text = data.kode_tran
        holder.tvTotal.text = data.total_harga
        holder.lanjut.setOnClickListener {
            listener.onClicked(data)
        }

    }

    interface Listeners{
        fun onClicked(data:Transaksi)
    }

}