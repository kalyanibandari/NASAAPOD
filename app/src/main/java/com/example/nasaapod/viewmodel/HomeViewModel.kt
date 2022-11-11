package com.example.nasaapod.viewmodel

import androidx.lifecycle.ViewModel
import com.example.nasaapod.data.db.ApodEntity
import com.example.nasaapod.data.repositories.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    /**
     * Get today's apod data from the api call
     */
    suspend fun getApod() = withContext(Dispatchers.IO) {
        homeRepository.getApodRequest()
    }

    /**
     * Get specific date data from api call
     */
    suspend fun getSpecificApod(day:Int, month:Int, year:Int) = withContext(Dispatchers.IO){
        homeRepository.getSpecificApodRequest(day,month,year)
    }

    /**
     * Insert data to db
     */
    suspend fun insertData(apodEntity: ApodEntity) = homeRepository.insertData(apodEntity)

    /**
     * Update data to db
     */
    suspend fun updateData(apodEntity: ApodEntity) = homeRepository.updateData(apodEntity)

    /**
     * Get specific date data from db
     */
    suspend fun getDataByDate(date: String) = withContext(Dispatchers.IO){
        homeRepository.getDataByDate(date)
    }

    suspend fun deleteData() = withContext(Dispatchers.IO){
        homeRepository.deleteData()
    }
}