package com.example.mobilearek.model

import com.google.gson.annotations.SerializedName

class Page {
    @SerializedName("current_page")
    var page = 0
    var last_page = 0
    var data = ArrayList<Mobil>()
}