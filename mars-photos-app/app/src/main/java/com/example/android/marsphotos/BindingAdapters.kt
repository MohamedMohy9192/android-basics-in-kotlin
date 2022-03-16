package com.example.android.marsphotos

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.android.marsphotos.network.MarsPhoto
import com.example.android.marsphotos.overview.PhotoGridAdapter

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let { url ->
        val imgUri = url.toUri()
            .buildUpon()
            .scheme("https")
            .build()
        imgView.load(imgUri) {
            // Sets the placeholder loading image to use while loading
            placeholder(R.drawable.loading_animation)
            // Sets an image to use if image loading fails
            error(R.drawable.ic_broken_image)
        }
    }
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<MarsPhoto>?) {
    // Retrieves the previously set adapter in the OverviewFragment
    val adapter = recyclerView.adapter as PhotoGridAdapter
    adapter.submitList(data)
}