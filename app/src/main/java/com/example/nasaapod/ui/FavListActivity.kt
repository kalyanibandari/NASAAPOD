package com.example.nasaapod.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nasaapod.R
import com.example.nasaapod.data.db.ApodEntity
import com.example.nasaapod.databinding.ActivityFavListBinding
import com.example.nasaapod.ui.adapter.FavoriteAdapter
import com.example.nasaapod.viewmodel.FavoriteViewModel
import com.example.nasaapod.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavListActivity : AppCompatActivity(), FavoriteAdapter.ItemDelegate {

    private lateinit var binding : ActivityFavListBinding
    private val favoriteViewModel : FavoriteViewModel by viewModels()
    private val homeViewModel : HomeViewModel by viewModels()
    private lateinit var adapter : FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fav_list)

        initAdapter()
        collectFavorite()
    }

    private fun initAdapter() {
        adapter = FavoriteAdapter(this)
        binding.favListView.layoutManager = LinearLayoutManager(this)
        binding.favListView.adapter = adapter
    }

    private fun collectFavorite() {
        lifecycleScope.launch {
            val dbData = favoriteViewModel.getFavData()
            dbData.observe(this@FavListActivity) {
                if (it.size == 0) {
                    binding.noFavoriteTextView.visibility = View.VISIBLE
                } else {
                    binding.noFavoriteTextView.visibility = View.GONE
                }
                adapter.updateFavList(it as MutableList<ApodEntity>)
            }
            binding.noFavoriteTextView.visibility = View.GONE
        }
    }

    override fun itemChanged(apodEntity: ApodEntity) {
        favoriteViewModel.updateApod(apodEntity)
    }
}