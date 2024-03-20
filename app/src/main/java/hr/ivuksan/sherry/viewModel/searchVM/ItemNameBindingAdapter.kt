package hr.ivuksan.sherry.viewModel.searchVM

import android.widget.TextView
import androidx.databinding.BindingAdapter
import hr.ivuksan.sherry.model.searchModel.SearchItem

object ItemNameBindingAdapter {

    @JvmStatic
    @BindingAdapter("nameByType")
    fun setNameByType(textView: TextView, searchItem: SearchItem?) {
        searchItem?.let {
            val textValue = when (searchItem.type) {
                SearchRecyclerViewAdapter.TYPE_ALBUM -> searchItem.album?.name
                SearchRecyclerViewAdapter.TYPE_ARTIST -> searchItem.artist?.name
                SearchRecyclerViewAdapter.TYPE_PLAYLIST -> searchItem.playlist?.name
                SearchRecyclerViewAdapter.TYPE_TRACK -> searchItem.track?.name
                else -> null
            }

            textView.text = textValue
        }
    }
}