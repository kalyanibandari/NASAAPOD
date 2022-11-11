package com.example.nasaapod.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ApodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val date: String?,
    val title: String?,
    val explanation: String?,
    val url: String?,
    val media_type: String?,
    var isFav: Boolean?
)