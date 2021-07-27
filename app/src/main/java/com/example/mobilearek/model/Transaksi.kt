package com.example.mobilearek.model

class Transaksi {
    var id=0
    var user_id=0
    var mobil_id=0
    var kode_tran=""
    var tgl_order=""
    var total_harga=""
    var status_tr=0
    var transfer=""
    var tgl_sewa=""
    var tgl_akhir_sewa=""
    var bukti_tf =""
    var lama_sewa=""
    var status_bayar=0
    var expired_at=""
    var created_at=""
    var updated_at=""
    var mobil = Mobil()
}