package com.example.mobilearek.room

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.mobilearek.model.Mobil


@Suppress("AndroidUnresolvedRoomSqlReference")
@Dao
interface DaoPesanan {

    @Insert(onConflict = REPLACE)
    fun insert(data: Mobil)

    @Delete
    fun delete(data: Mobil)

    @Update
    fun update(data: Mobil): Int

    @Query("SELECT * FROM keranjang ORDER BY id ASC")
    fun getAll(): List<Mobil>

    @Query("SELECT * FROM keranjang WHERE id = :id LIMIT 1")
    fun getMobil(id: Int): Mobil

    @Query("DELETE FROM keranjang")
    fun deleteAll(): Int
}