package hr.ivuksan.sherry.viewModel.albumVM

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.ivuksan.sherry.databinding.AlbumItemBinding
import hr.ivuksan.sherry.model.Section
import hr.ivuksan.sherry.model.albumModel.Album

class NewReleasesRecyclerViewAdapter(private val albumClickListener: AlbumClickListener): RecyclerView.Adapter<NewReleasesRecyclerViewAdapter.NewReleasesViewHolder>() {

    private var newReleasesList = Section.NewReleases(null)

    class NewReleasesViewHolder(val binding: AlbumItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album){
            binding.albumName.text = album.name
            Picasso.get()
                .load(album.images.firstOrNull()?.url?.toUri())
                .into(binding.albumImage)
            val artistList = album.artists.joinToString(", ") {it.name}
            binding.albumArtists.text = artistList
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewReleasesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AlbumItemBinding.inflate(inflater, parent, false)
        return NewReleasesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewReleasesViewHolder, position: Int) {
        val album = newReleasesList.albums?.items?.get(position)
        if (album != null) {
            holder.bind(album)
        }
        holder.itemView.setOnClickListener {
            if (album != null) {
                albumClickListener.onAlbumClicked(album.id)
            }
        }
    }

    override fun getItemCount(): Int {
        return newReleasesList.albums?.items?.size ?: 0
    }

    fun setNewReleases(newReleasesList: Section.NewReleases){
        this.newReleasesList = newReleasesList
        notifyDataSetChanged()
    }
}