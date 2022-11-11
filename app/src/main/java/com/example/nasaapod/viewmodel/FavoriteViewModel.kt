package com.example.nasaapod.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasaapod.data.db.ApodEntity
import com.example.nasaapod.data.repositories.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    /**
     * Get list of favorites from db
     */
    suspend fun getFavData() = withContext(Dispatchers.IO){
        homeRepository.getFavData()
    }

    /**
     * Update data to db
     */
    fun updateApod(apodEntity: ApodEntity) = viewModelScope.launch {
        homeRepository.updateData(apodEntity)
    }
}