package hr.ivuksan.sherry.viewModel.playlistVM

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.ivuksan.sherry.viewModel.trackVM.TrackClickListener
import hr.ivuksan.sherry.databinding.TrackItemBinding
import hr.ivuksan.sherry.model.trackModel.PlaylistTrackInfo

class PlaylistTracksRecyclerViewAdapter(private val trackClickListener: TrackClickListener): RecyclerView.Adapter<PlaylistTracksRecyclerViewAdapter.TracksViewHolder>() {

    private var tracksInfo: List<PlaylistTrackInfo> = emptyList()

    class TracksViewHolder(val binding: TrackItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(tracksInfo: PlaylistTrackInfo) {
            binding.trackName.text = tracksInfo.track.name
            binding.trackArtists.text = tracksInfo.track.artists.joinToString(", ") {it.name}
            binding.trackLength.text =
                String.format("%02d:%02d",
                    (tracksInfo.track.duration_ms / 1000 / 60).toInt(),
                    (tracksInfo.track.duration_ms / 1000 % 60).toInt())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TrackItemBinding.inflate(inflater, parent, false)
        return TracksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        val trackInfo = tracksInfo[position]
        holder.bind(trackInfo)
        holder.itemView.setOnClickListener {
            trackClickListener.onTrackClicked(trackInfo.track.id)
        }
    }

    override fun getItemCount(): Int {
        return tracksInfo.size
    }

    fun setTracks(tracksInfo: List<PlaylistTrackInfo>) {
        this.tracksInfo = tracksInfo
        notifyDataSetChanged()
    }
}