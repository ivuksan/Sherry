package hr.ivuksan.sherry.viewModel.searchVM

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.ivuksan.sherry.databinding.SearchTrackBinding
import hr.ivuksan.sherry.model.trackModel.Track

class SearchToAddRecyclerViewAdapter(private val addTrackClickListener: AddClickListener): RecyclerView.Adapter<SearchToAddRecyclerViewAdapter.SearchToAddViewHolder>() {

    private var tracks: List<Track> = emptyList()

    class SearchToAddViewHolder(val binding: SearchTrackBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(track: Track){
            binding.track = track
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchToAddViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SearchToAddViewHolder(SearchTrackBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: SearchToAddViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener {
            addTrackClickListener.onAddTrackClicked(track.uri)
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    fun setTracks(tracks: List<Track>){
        this.tracks = tracks
        notifyDataSetChanged()
    }
}