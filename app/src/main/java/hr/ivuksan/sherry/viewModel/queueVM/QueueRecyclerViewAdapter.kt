package hr.ivuksan.sherry.viewModel.queueVM

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.ivuksan.sherry.databinding.QueueItemBinding
import hr.ivuksan.sherry.model.trackModel.Track

class QueueRecyclerViewAdapter: RecyclerView.Adapter<QueueRecyclerViewAdapter.QueueViewHolder>() {

    private var queueSongs: List<Track> = emptyList()

    class QueueViewHolder(val binding: QueueItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(queueSong: Track){
            binding.textViewSongTitle.text = queueSong.name
            val artistList = queueSong.artists.joinToString(", ") {it.name}
            binding.textViewSongArtist.text = artistList
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QueueViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = QueueItemBinding.inflate(inflater, parent, false)
        return QueueViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QueueViewHolder, position: Int) {
        val queueSong = queueSongs[position]
        holder.bind(queueSong)
    }

    override fun getItemCount(): Int {
        return queueSongs.size
    }

    fun setTracks(queueSongs: List<Track>){
        this.queueSongs = queueSongs
        notifyDataSetChanged()
    }
}