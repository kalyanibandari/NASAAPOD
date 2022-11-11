package com.example.nasaapod.data.repositories

import androidx.lifecycle.LiveData
import com.example.nasaapod.data.db.ApodDatabse
import com.example.nasaapod.data.db.ApodEntity
import javax.inject.Inject
import javax.inject.Singleton
import com.example.nasaapod.data.network.ApodApi
import com.example.nasaapod.data.network.SafeApiRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
class HomeRepository @Inject constructor(
    private val api: ApodApi,
    private val db: ApodDatabse
) : SafeApiRequest() {

    /**
     * Get today's apod data from the api call
     */
    suspend fun getApodRequest(): ApodEntity {
        return apiRequest {
            api.getApod()
        }
    }

    /**
     * Get specific date data from api call
     */
    suspend fun getSpecificApodRequest(day: Int, month: Int, year: Int): ApodEntity {
        val correctMonth = month + 1
        val dateFormat = "$year-$correctMonth-$day"
        return apiRequest { api.getSpecificApod(date = dateFormat) }
    }

    /**
     * Insert data to db
     */
    suspend fun insertData(appEntity: ApodEntity) = db.apodDao().addApod(appEntity)

    /**
     * Update data to db
     */
    suspend fun updateData(appEntity: ApodEntity) = db.apodDao().updateApod(appEntity)

    /**
     * Get list of favorites from db
     */
    suspend fun getFavData(): LiveData<MutableList<ApodEntity>> {
        return withContext(Dispatchers.IO) {
            db.apodDao().getFavorites()
        }
    }

    /**
     * Get specific date data from db
     */
    suspend fun getDataByDate(date: String): ApodEntity {
        return withContext(Dispatchers.IO) {
            db.apodDao().getDataByDate(date)
        }
    }

    suspend fun deleteData() {
        return withContext(Dispatchers.IO) {
            db.apodDao().deleteData()
        }
    }
}