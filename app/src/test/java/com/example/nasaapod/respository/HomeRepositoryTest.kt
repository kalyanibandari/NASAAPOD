package com.example.nasaapod.respository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nasaapod.data.db.ApodDatabse
import com.example.nasaapod.data.db.ApodEntity
import com.example.nasaapod.data.network.ApodApi
import com.example.nasaapod.data.repositories.HomeRepository
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

@RunWith(JUnit4::class)
class HomeRepositoryTest {
    lateinit var homeRepository: HomeRepository

    @Mock
    lateinit var api: ApodApi

    @Mock
    lateinit var apodEntity: ApodEntity

    @Mock
    lateinit var apod : LiveData<MutableList<ApodEntity>>

    @Mock
    lateinit var db: ApodDatabse

    private val _res: LiveData<MutableList<ApodEntity>> = MutableLiveData()
    val res: LiveData<MutableList<ApodEntity>> = _res

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        homeRepository = HomeRepository(api, db)
    }

    @Test
    fun `get today's apod data test`() {
        runBlocking {
            Mockito.`when`(api.getApod()).thenReturn(Response.success(apodEntity))
            val response = homeRepository.getApodRequest()
            assertEquals(response, Response.success(apodEntity).body())
        }
    }

}