package com.example.nasaapod.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ApodEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ApodDatabse : RoomDatabase(){
    abstract fun apodDao() : ApodDao
}