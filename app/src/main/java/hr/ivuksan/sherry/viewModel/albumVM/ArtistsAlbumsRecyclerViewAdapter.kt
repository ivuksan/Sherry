package hr.ivuksan.sherry.viewModel.albumVM

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.ivuksan.sherry.databinding.AlbumItemBinding
import hr.ivuksan.sherry.model.Section
import hr.ivuksan.sherry.model.albumModel.Album

class ArtistsAlbumsRecyclerViewAdapter(private val albumClickListener: AlbumClickListener): RecyclerView.Adapter<ArtistsAlbumsRecyclerViewAdapter.ArtistsAlbumViewHolder>() {

    private var artistsAlbumsList = Section.ArtistsAlbums(emptyList())

    class ArtistsAlbumViewHolder(val binding: AlbumItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(album: Album) {
            binding.albumName.text = album.name
            Picasso.get()
                .load(album.images.firstOrNull()?.url?.toUri())
                .into(binding.albumImage)
            val artistList = album.artists.joinToString(", ") {it.name}
            binding.albumArtists.text = artistList
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistsAlbumViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AlbumItemBinding.inflate(inflater, parent, false)
        return ArtistsAlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArtistsAlbumViewHolder, position: Int) {
        val album = artistsAlbumsList.items[position]
        holder.bind(album)
        holder.itemView.setOnClickListener {
            albumClickListener.onAlbumClicked(album.id)
        }
    }

    override fun getItemCount(): Int {
        return artistsAlbumsList.items.size
    }

    fun setArtistsAlbums(artistsAlbumsList: Section.ArtistsAlbums){
        this.artistsAlbumsList = artistsAlbumsList
        notifyDataSetChanged()
    }
}