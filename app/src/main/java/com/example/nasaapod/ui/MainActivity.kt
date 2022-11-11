package com.example.nasaapod.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.nasaapod.R
import com.example.nasaapod.data.db.ApodEntity
import com.example.nasaapod.databinding.ActivityMainBinding
import com.example.nasaapod.util.DateUtil
import com.example.nasaapod.util.NoInternetException
import com.example.nasaapod.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var item : ApodEntity
    private lateinit var updatedItem : ApodEntity
    private lateinit var binding : ActivityMainBinding
    private val homeViewModel : HomeViewModel by viewModels()
    private var constraintSet: ConstraintSet = ConstraintSet()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.webView.settings.javaScriptEnabled = true
        binding.progressBar.visibility = View.VISIBLE


        binding.imgFavIcon.setOnClickListener{
            if (!binding.isFavorite){
                binding.isFavorite = true
                Toast.makeText(this,R.string.added_to_favorites, Toast.LENGTH_SHORT).show()
            }else{
                binding.isFavorite = false
                Toast.makeText(this,R.string.removed_from_favorites, Toast.LENGTH_SHORT).show()
            }
            updatedItem.isFav = binding.isFavorite

            lifecycleScope.launch{
                updateData(updatedItem)
            }
        }

        /**
         * Opening DatePickerDialog on click of calendar floating action button
         * and requesting data for selected date
         */
        binding.fab.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(this, { view, currentYear, monthOfYear, dayOfMonth ->
                binding.progressBar.visibility = View.VISIBLE
                lifecycleScope.launch{
                    getSpecificApodRes(dayOfMonth,monthOfYear, currentYear)
                }
            }, year, month, day)
            dpd.datePicker.maxDate = System.currentTimeMillis()
            dpd.show()
        }

        /**
         * Fetch today's data
         */
        lifecycleScope.launch{
            getTodaysData()
        }
    }

    /**
     * Fetching today's data from either api or db based on data availability in db.
     */
    private suspend fun getTodaysData() {
        val todayDate = DateUtil.todayDate()
        val todayDataFromDb = homeViewModel.getDataByDate(todayDate)
        if(todayDataFromDb!= null){
            updatedItem = todayDataFromDb
            prepareUI(updatedItem)
        }else{
            try{
                item = homeViewModel.getApod()
            }catch (e: NoInternetException){
                e.printStackTrace()
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
            }
            if(isItemInitialized()){
                item.isFav = false
                homeViewModel.insertData(item)
                val fetchedDataFromDb = homeViewModel.getDataByDate(todayDate)
                updatedItem = fetchedDataFromDb
                prepareUI(updatedItem)
            }else{
                binding.apply {
                    imgFavIcon.visibility = View.GONE
                    errorTextView.visibility = View.VISIBLE
                    isFavorite = false
                }
            }
        }
        binding.progressBar.visibility = View.GONE
    }

    private fun prepareUI(item: ApodEntity) {
        binding.apply {
            tvDateAndTitle.text = buildString {
                append(item.date)
                append(": ")
                append(item.title)
            }
            tvDescription.text = item.explanation
            isFavorite = item.isFav!!
        }
        setMediaView(item.media_type!!, item.url!!)
    }

    /**
     * Checking if item is initialized or not.
     */
    private fun isItemInitialized() : Boolean{
        return this::item.isInitialized
    }

    /**
     * Fetching data of specific date from either api or db based on data availability in db.
     */
    private suspend fun getSpecificApodRes(dayOfMonth: Int, monthOfYear: Int, year: Int) {
        val correctMonth = monthOfYear+1
        val dateFormat = "$year-$correctMonth-$dayOfMonth"
        val finalDate = DateUtil.formatTheDate(dateFormat)
        val specificDateDataFromDb = homeViewModel.getDataByDate(finalDate)
        if(specificDateDataFromDb != null){
            updatedItem = specificDateDataFromDb
            prepareUI(updatedItem)
        }else{
            try{
                item = homeViewModel.getSpecificApod(dayOfMonth, monthOfYear, year)
            }catch (e: NoInternetException){
                e.printStackTrace()
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
            }
            if(isItemInitialized()){
                item.isFav = false
                homeViewModel.insertData(item)
                val fetchedSpecDataFromDb = homeViewModel.getDataByDate(finalDate)
                updatedItem = fetchedSpecDataFromDb
                prepareUI(updatedItem)
            }else{
                Toast.makeText(this@MainActivity,R.string.internet_unavailable, Toast.LENGTH_LONG).show()
            }
        }
        binding.progressBar.visibility = View.GONE
    }

    /**
     * Updating data to db
     */
    private suspend fun updateData(item : ApodEntity){
        homeViewModel.updateData(item)
    }

    private fun setMediaView(mediaType: String, mediaUrl: String) {
        if(mediaType == "video"){
            binding.imageView.visibility = View.GONE
            binding.webView.visibility = View.VISIBLE
            constraintSet.clone(binding.mainConstraintLayout)
            constraintSet.connect(
                R.id.tvDateAndTitle, ConstraintSet.TOP,
                R.id.webView, ConstraintSet.BOTTOM,0)
            constraintSet.applyTo(binding.mainConstraintLayout)
            binding.webView.loadUrl(mediaUrl)
        }else if(mediaType == "image"){
            binding.imageView.visibility = View.VISIBLE
            binding.webView.visibility = View.GONE
            constraintSet.clone(binding.mainConstraintLayout)
            constraintSet.connect(
                R.id.tvDateAndTitle, ConstraintSet.TOP,
                R.id.imageView, ConstraintSet.BOTTOM,0)
            constraintSet.applyTo(binding.mainConstraintLayout)
            Glide.with(binding.imageView).load(mediaUrl).centerCrop().into(binding.imageView)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.favList -> {
                startActivity(Intent(this, FavListActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}





