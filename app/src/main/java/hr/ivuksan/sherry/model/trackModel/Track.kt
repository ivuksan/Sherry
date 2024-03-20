package hr.ivuksan.sherry.model.trackModel

import hr.ivuksan.sherry.model.albumModel.Album
import hr.ivuksan.sherry.model.artistModel.Artist

data class Track(
    val id: String,
    val name: String,
    val album: Album,
    val artists: List<Artist>,
    val duration_ms: Int,
    val uri: String
)