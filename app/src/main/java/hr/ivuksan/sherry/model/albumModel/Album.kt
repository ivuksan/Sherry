package hr.ivuksan.sherry.model.albumModel

import hr.ivuksan.sherry.model.artistModel.Artist
import hr.ivuksan.sherry.model.trackModel.AlbumTrackInfo

data class Album(
    val id: String,
    val name: String,
    val images: List<AlbumCover>,
    val tracks: AlbumTrackInfo,
    val artists: List<Artist>
)