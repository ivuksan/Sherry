package hr.ivuksan.sherry.viewModel.artistVM

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import hr.ivuksan.sherry.databinding.ArtistItemBinding
import hr.ivuksan.sherry.model.Section
import hr.ivuksan.sherry.model.artistModel.Artist

class ArtistsRecyclerViewAdapter: RecyclerView.Adapter<ArtistsRecyclerViewAdapter.ArtistViewHolder>() {

    private var artistsInfo = Section.Artists(null)

    class ArtistViewHolder(val binding: ArtistItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(artist: Artist){
            binding.artistName.text = artist.name
            Glide.with(itemView.context)
                .load(artist.images.firstOrNull()?.url?.toUri())
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(binding.artistImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ArtistItemBinding.inflate(inflater, parent, false)
        return ArtistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist = artistsInfo.artists?.items?.get(position)
        if (artist != null) {
            holder.bind(artist)
        }
    }

    override fun getItemCount(): Int {
        return artistsInfo.artists?.items?.size ?: 0
    }

    fun setArtists(artists: Section.Artists){
        this.artistsInfo = artists
        notifyDataSetChanged()
    }
}