package hr.ivuksan.sherry.viewModel.searchVM

import androidx.databinding.BindingAdapter
import hr.ivuksan.sherry.model.searchModel.SearchItem
import android.widget.ImageView
import com.bumptech.glide.Glide
import hr.ivuksan.sherry.R

object ItemImageBindingAdapter {

    @JvmStatic
    @BindingAdapter("imageUrlByType")
    fun loadImageByType(imageView: ImageView, searchItem: SearchItem?) {
        searchItem?.let {
            val imageUrl = when (searchItem.type) {
                SearchRecyclerViewAdapter.TYPE_ALBUM -> searchItem.album?.images?.getOrNull(0)?.url
                SearchRecyclerViewAdapter.TYPE_ARTIST -> searchItem.artist?.images?.getOrNull(0)?.url
                SearchRecyclerViewAdapter.TYPE_PLAYLIST -> searchItem.playlist?.images?.getOrNull(0)?.url
                SearchRecyclerViewAdapter.TYPE_TRACK -> searchItem.track?.album?.images?.getOrNull(0)?.url
                else -> null
            }

            Glide.with(imageView.context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder) // Placeholder image
                .into(imageView)
        }
    }
}

