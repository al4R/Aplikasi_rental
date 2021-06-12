package com.example.mobilearek.model

class Transaksi {
    lateinit var user_id:String
    lateinit var tgl_order:String
    lateinit var total_harga:String
    lateinit var transfer:String
    lateinit var tgl_sewa:String
    lateinit var tgl_akhir_sewa:String
    lateinit var lama_sewa:String
    var mobils = ArrayList<Item>()
    class Item{
        lateinit var id:String
        lateinit var harga_sewa:String

    }

}