package hr.ivuksan.sherry.viewModel.albumVM

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.ivuksan.sherry.viewModel.trackVM.TrackClickListener
import hr.ivuksan.sherry.databinding.TrackItemBinding
import hr.ivuksan.sherry.model.trackModel.Track

class AlbumTracksRecyclerViewAdapter(private val trackClickListener: TrackClickListener): RecyclerView.Adapter<AlbumTracksRecyclerViewAdapter.TracksViewHolder>() {

    private var tracks: List<Track> = emptyList()

    class TracksViewHolder(val binding: TrackItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(track: Track) {
            binding.trackName.text = track.name
            binding.trackArtists.text = track.artists.joinToString(", ") {it.name}
            binding.trackLength.text =
                String.format("%02d:%02d",
                    (track.duration_ms / 1000 / 60).toInt(),
                    (track.duration_ms / 1000 % 60).toInt())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TrackItemBinding.inflate(inflater, parent, false)
        return TracksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener {
            trackClickListener.onTrackClicked(track.id)
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    fun setTracks(tracks: List<Track>) {
        this.tracks = tracks
        notifyDataSetChanged()
    }
}