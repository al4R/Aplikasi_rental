package com.example.mobilearek.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "keranjang")
public class Mobil{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idTb")
    public int idTb;

    public int id;
    public String model;
    public String harga;
    public String image;
    public String merk;
    public String tahun;
    public String transmisi;
    public String kapasitas;
    public String deskripsi;
    public String no_kendaraan;
    public int status;
    public String created_at;
    public String updated_at;

    public String total;
    public String tglSewa;
    public String tglKembali;
    public String TglS;
    public String TglK;
    public String lamaSewa;

}