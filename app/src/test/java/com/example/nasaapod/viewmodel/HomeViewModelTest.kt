package com.example.nasaapod.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.nasaapod.MainCoroutinesRule
import com.example.nasaapod.data.db.ApodEntity
import com.example.nasaapod.data.repositories.HomeRepository
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutinesRule = MainCoroutinesRule()

    private lateinit var homeViewmodel: HomeViewModel

    @Mock
    lateinit var apodEntity: ApodEntity

    @Mock
    lateinit var repository: HomeRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        homeViewmodel = HomeViewModel(repository)
    }

    @Test
    fun getTodayApodTest() {
        runBlocking {
            Mockito.`when`(repository.getApodRequest())
                .thenReturn(apodEntity)
            val result = homeViewmodel.getApod()
            assertEquals(result, apodEntity)
        }
    }

    @Test
    fun getspecificDateApodTest() {
        runBlocking {
            Mockito.`when`(repository.getSpecificApodRequest(10, 10, 2022))
                .thenReturn(apodEntity)
            val result = homeViewmodel.getSpecificApod(10, 10, 2022)
            assertEquals(result, apodEntity)
        }
    }

    @Test
    fun getspecificDateApodFromDBTest() {
        runBlocking {
            Mockito.`when`(repository.getDataByDate("2022-10-10"))
                .thenReturn(apodEntity)
            val result = homeViewmodel.getDataByDate("2022-10-10")
            assertEquals(result, apodEntity)
        }
    }

}