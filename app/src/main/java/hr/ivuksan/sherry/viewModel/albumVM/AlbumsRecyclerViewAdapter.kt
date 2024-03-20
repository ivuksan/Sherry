package hr.ivuksan.sherry.viewModel.albumVM

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.ivuksan.sherry.databinding.AlbumItemBinding
import hr.ivuksan.sherry.model.Section
import hr.ivuksan.sherry.model.albumModel.AlbumInfo

class AlbumsRecyclerViewAdapter(private val albumClickListener: AlbumClickListener): RecyclerView.Adapter<AlbumsRecyclerViewAdapter.AlbumInfoViewHolder>() {

    private var albumsInfo = Section.Albums(emptyList())

    class AlbumInfoViewHolder(val binding: AlbumItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(albumInfo: AlbumInfo){
            binding.albumName.text = albumInfo.album.name
            Picasso.get()
                .load(albumInfo.album.images.firstOrNull()?.url?.toUri())
                .into(binding.albumImage)
            val artistList = albumInfo.album.artists.joinToString(", ") {it.name}
            binding.albumArtists.text = artistList
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumInfoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AlbumItemBinding.inflate(inflater, parent, false)
        return AlbumInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumInfoViewHolder, position: Int) {
        val album = albumsInfo.items[position]
        holder.bind(album)
        holder.itemView.setOnClickListener {
            albumClickListener.onAlbumClicked(album.album.id)
        }
    }

    override fun getItemCount(): Int {
        return albumsInfo.items.size
    }

    fun setAlbums(albums: Section.Albums){
        this.albumsInfo = albums
        notifyDataSetChanged()
    }
}