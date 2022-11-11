package com.example.nasaapod.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
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
class FavoriteViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutinesRule = MainCoroutinesRule()

    private lateinit var favViewmodel: FavoriteViewModel

    @Mock
    lateinit var apod: LiveData<MutableList<ApodEntity>>

    @Mock
    lateinit var repository: HomeRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        favViewmodel = FavoriteViewModel(repository)
    }

    @Test
    fun getFavDataTest() {
        runBlocking {
            Mockito.`when`(repository.getFavData())
                .thenReturn(apod)
            val result = favViewmodel.getFavData()
            assertEquals(result, apod)
        }
    }
}