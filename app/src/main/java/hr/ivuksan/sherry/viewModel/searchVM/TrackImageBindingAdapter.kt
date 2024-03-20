package hr.ivuksan.sherry.viewModel.searchVM

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import hr.ivuksan.sherry.R
import hr.ivuksan.sherry.model.trackModel.Track

object TrackImageBindingAdapter {

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(imageView: ImageView, track: Track?) {
        Glide.with(imageView.context)
            .load(track?.album?.images?.firstOrNull()?.url)
            .placeholder(R.drawable.placeholder) // Placeholder image
            .into(imageView)
    }
}