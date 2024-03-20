package hr.ivuksan.sherry.viewModel.playlistVM

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.ivuksan.sherry.R
import hr.ivuksan.sherry.databinding.PlaylistItemBinding
import hr.ivuksan.sherry.model.playlistModel.Playlist
import hr.ivuksan.sherry.model.Section

class PlaylistsRecyclerViewAdapter(private val playlistClickListener: PlaylistClickListener): RecyclerView.Adapter<PlaylistsRecyclerViewAdapter.PlaylistViewHolder>() {

    private var playlists = Section.Playlists(emptyList())

    class PlaylistViewHolder(val binding: PlaylistItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(playlist: Playlist){
            binding.playlistName.text = playlist.name
            Picasso.get()
                .load(playlist.images.firstOrNull()?.url?.toUri())
                .placeholder(R.drawable.placeholder)
                .into(binding.playlistImage)
            if (playlist.description.isEmpty()){
                binding.playlistDescription.text = ""
            }else {
                binding.playlistDescription.text = playlist.description
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PlaylistItemBinding.inflate(inflater, parent, false)
        return PlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists.items[position]
        holder.bind(playlist)
        holder.itemView.setOnClickListener {
            playlistClickListener.onPlaylistClicked(playlist.id)
        }
    }

    override fun getItemCount(): Int {
        return playlists.items.size
    }

    fun setPlaylists(playlists: Section.Playlists){
        this.playlists = playlists
        notifyDataSetChanged()
    }
}