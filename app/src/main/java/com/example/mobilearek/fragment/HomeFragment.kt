package com.example.mobilearek.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilearek.R
import com.example.mobilearek.adapter.AdapterProduk
import com.example.mobilearek.model.Produk

class HomeFragment : Fragment() {
    lateinit var rvProduk: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater.inflate(R.layout.fragment_home, container, false)

        rvProduk = view.findViewById(R.id.rv_produk)

        val layoutManager = GridLayoutManager (activity,2,GridLayoutManager.VERTICAL,false)
        rvProduk.adapter = AdapterProduk(arrayProduk)
        rvProduk.layoutManager = layoutManager
        return view
    }
    val arrayProduk:ArrayList<Produk>get() {
        val arr = ArrayList<Produk>()
        val p1 = Produk()
        p1.nama = "Brio"
        p1.harga= "Rp.2000"
        p1.gambar= R.drawable.ic_history

        val p2 = Produk()
        p2.nama = "Brio2"
        p2.harga= "Rp.20002"
        p2.gambar= R.drawable.ic_history

        arr.add(p1)
        arr.add(p2)
        return arr
    }
}