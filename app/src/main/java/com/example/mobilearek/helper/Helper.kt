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
        val formatLama = "d/MM/yyyy k.m"
        val formatBaru = "dd MMMM yyyy kk.mm"
        val dateFormat = SimpleDateFormat(formatLama)
        val convert = dateFormat.parse(tgl)
        dateFormat.applyPattern(formatBaru)
        val newFormat = dateFormat.format(convert)
        tv.text = newFormat
    }
    @SuppressLint("SimpleDateFormat")
    fun ubahformatTgl2(tgl:String,s:String){
        val formatLama = "dd MMMM yyyy kk.mm"
        val formatBaru = "d/MM/yyyy k.m"
        val dateFormat = SimpleDateFormat(formatLama)
        val convert = dateFormat.parse(tgl)
        dateFormat.applyPattern(formatBaru)
        val newFormat = dateFormat.format(convert)
        s.plus(newFormat)

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