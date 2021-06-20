package com.example.mobilearek.model

class ResponModel {
    var success = 0
    lateinit var message:String
    var user = User()
    var mobil:ArrayList<Mobil> = ArrayList()
    var transaksi:ArrayList<Transaksi> = ArrayList()
    
}