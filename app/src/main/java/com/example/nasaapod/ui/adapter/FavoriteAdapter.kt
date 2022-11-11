package com.example.nasaapod.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nasaapod.data.db.ApodEntity
import com.example.nasaapod.data.network.MediaType
import com.example.nasaapod.databinding.FavListItemBinding

class FavoriteAdapter(private var delegate: ItemDelegate) :
    RecyclerView.Adapter<FavoriteAdapter.MyViewHolder>() {

    private lateinit var mContext: Context
    private var favList = mutableListOf<ApodEntity>()

    interface ItemDelegate {
        fun itemChanged(apodEntity: ApodEntity)
    }

    private fun setDelegate(delegate: ItemDelegate) {
        this.delegate = delegate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
        val binding = FavListItemBinding.inflate(itemView, parent, false)
        mContext = parent.context
        this.setDelegate(delegate)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(favList[position])
    }

    override fun getItemCount(): Int {
        return favList.size
    }

    inner class MyViewHolder(private val binding: FavListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetJavaScriptEnabled")
        fun bindData(apodEntity: ApodEntity) {
            binding.type = apodEntity.media_type
            if(binding.type == MediaType.IMAGE.type){
                Glide.with(binding.imageImgView).load(apodEntity.url).centerCrop().into(binding.imageImgView)
            }else{
                binding.webView.settings.javaScriptEnabled = true
                binding.webView.loadUrl(apodEntity.url!!)
            }
            binding.titleTxtView.text = buildString {
                append(apodEntity.date)
                append(": ")
                append(apodEntity.title)
            }
            binding.isFavorite = apodEntity.isFav!!
            binding.favoriteImgView.setOnClickListener {
                apodEntity.isFav = !apodEntity.isFav!!
                binding.isFavorite = apodEntity.isFav!!
                delegate.itemChanged(apodEntity)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateFavList(updatedData: MutableList<ApodEntity>){
        this.favList = updatedData
        notifyDataSetChanged()
    }
}