package hr.ivuksan.sherry.viewModel.searchVM

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.ivuksan.sherry.databinding.SearchItemBinding
import hr.ivuksan.sherry.model.searchModel.SearchItem
import hr.ivuksan.sherry.model.searchModel.SearchResponse

class SearchRecyclerViewAdapter : RecyclerView.Adapter<SearchRecyclerViewAdapter.SearchItemViewHolder>() {

    private var combinedList: List<SearchItem> = emptyList()

    companion object {
        const val TYPE_ALBUM = 1
        const val TYPE_ARTIST = 2
        const val TYPE_PLAYLIST = 3
        const val TYPE_TRACK = 4
    }

    class SearchItemViewHolder(val binding: SearchItemBinding) :
        RecyclerView.ViewHolder(binding.root){
        fun bind(searchItem: SearchItem?) {
            binding.searchItem = searchItem
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SearchItemViewHolder(SearchItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {
        holder.bind(combinedList[position])
    }

    override fun getItemCount(): Int {
        return combinedList.size
    }

    fun setSearchResponses(searchResponse: SearchResponse){
        val tempList = mutableListOf<SearchItem>().apply {
            addAll(searchResponse.albums.items.map {
                SearchItem(TYPE_ALBUM, it, null, null, null) })
            addAll(searchResponse.artists.items.map {
                SearchItem(TYPE_ARTIST, null, it, null, null) })
            addAll(searchResponse.playlists.items.map {
                SearchItem(TYPE_PLAYLIST, null, null, it, null) })
            addAll(searchResponse.tracks.items.map {
                SearchItem(TYPE_TRACK, null, null, null, it) })
        }
        tempList.shuffle()
        combinedList = tempList
        notifyDataSetChanged()
    }
}