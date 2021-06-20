package com.example.mobilearek.helper

import android.annotation.SuppressLint
import android.widget.TextView
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "UNUSED_VALUE")
class Helper{
    @SuppressLint("SimpleDateFormat")
    fun ubahFormatTgl(tgl:String, tv:TextView){
        val formatLama = "d MM yyyy"
        val formatBaru = "dd MMMM yyyy"
        val dateFormat = SimpleDateFormat(formatLama)
        val convert = dateFormat.parse(tgl)
        dateFormat.applyPattern(formatBaru)
        val newFormat = dateFormat.format(convert)
        tv.text = newFormat
    }
    @SuppressLint("SimpleDateFormat")
    fun ubahFormatJam(jam:String, tv:TextView){
        val formatLama = "k.m"
        val formatBaru = "kk.mm"
        val dateFormat = SimpleDateFormat(formatLama)
        val convert = dateFormat.parse(jam)
        dateFormat.applyPattern(formatBaru)
        dateFormat.applyPattern(formatBaru)
        val newFormat = dateFormat.format(convert)
        tv.text = newFormat
    }

}