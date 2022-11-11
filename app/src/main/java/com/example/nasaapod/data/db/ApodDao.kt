package com.example.nasaapod.data.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ApodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addApod(apod: ApodEntity)

    @Update
    suspend fun updateApod(apod: ApodEntity)

    @Query("SELECT * FROM ApodEntity where date == :date")
    fun getDataByDate(date: String) : ApodEntity

    @Query("SELECT * FROM ApodEntity where isFav=1 order by date desc")
    fun getFavorites(): LiveData<MutableList<ApodEntity>>

    @Query("DELETE from ApodEntity")
    fun deleteData()

    @Query("SELECT * FROM ApodEntity")
    fun getApod(): LiveData<List<ApodEntity>>
}