package com.example.mobilearek.adapter

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilearek.R
import com.example.mobilearek.activity.DetailMobilActivity
import com.example.mobilearek.activity.DetailPesananActivity
import com.example.mobilearek.activity.PesananActivity
import com.example.mobilearek.model.Mobil
import com.example.mobilearek.room.MyDatabase
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AdapterPesanan (var activity: Activity, var data:ArrayList<Mobil>, var listener: Listeners): RecyclerView.Adapter<AdapterPesanan.Holder>() {

    class Holder(view: View): RecyclerView.ViewHolder(view){
        val tvMobil = view.findViewById<TextView>(R.id.tv_mobil)
        val tvTotal = view.findViewById<TextView>(R.id.tv_total)
        val tvStatus = view.findViewById<TextView>(R.id.tv_status)
        val tvUpdate = view.findViewById<TextView>(R.id.tv_update)
        val tvCancel = view.findViewById<TextView>(R.id.tv_cancel)
        val idTr = view.findViewById<CardView>(R.id.id_tr)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_pesanan,parent,false)
        return Holder(view)
    }
    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.tvMobil.text = data[position].merk+" "+data[position].name
        holder.tvTotal.text = "Rp."+data[position].total

        holder.idTr.setOnClickListener{
            val intent = Intent(activity, DetailPesananActivity::class.java)
            val str = Gson().toJson(data[position],Mobil::class.java)
            intent.putExtra("extra",str)
            activity.startActivity(intent)
        }

        holder.tvUpdate.setOnClickListener {
            val intent = Intent(activity, DetailMobilActivity::class.java)
            val str = Gson().toJson(data[position],Mobil::class.java)
            intent.putExtra("extra",str)
            activity.startActivity(intent)
        }

        holder.tvCancel.setOnClickListener {
           delete(data[position])
            listener.onDelete(position)
        }
    }
    interface Listeners{
        fun onDelete(position: Int)
    }
    private fun delete(data : Mobil){
        val myDb = MyDatabase.getInstance(activity)
        CompositeDisposable().add(Observable.fromCallable { myDb!!.daoPesan().delete(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {

                Log.d("respons", "data deleted")
            })
    }
}