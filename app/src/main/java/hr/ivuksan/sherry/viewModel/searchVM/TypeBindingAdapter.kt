package hr.ivuksan.sherry.viewModel.searchVM

import android.widget.TextView
import androidx.databinding.BindingAdapter
import hr.ivuksan.sherry.model.searchModel.SearchItem

object TypeBindingAdapter {

    @JvmStatic
    @BindingAdapter("typeByType")
    fun setTypeByType(textView: TextView, searchItem: SearchItem?) {
        searchItem?.let {
            val textValue = when (searchItem.type) {
                SearchRecyclerViewAdapter.TYPE_ALBUM -> "Album"
                SearchRecyclerViewAdapter.TYPE_ARTIST -> "Artist"
                SearchRecyclerViewAdapter.TYPE_PLAYLIST -> "Playlist"
                SearchRecyclerViewAdapter.TYPE_TRACK -> "Track"
                else -> null
            }

            textView.text = textValue
        }
    }
}