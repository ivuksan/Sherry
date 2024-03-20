package hr.ivuksan.sherry.model.searchModel

import hr.ivuksan.sherry.model.albumModel.Album
import hr.ivuksan.sherry.model.artistModel.Artist
import hr.ivuksan.sherry.model.playlistModel.Playlist
import hr.ivuksan.sherry.model.trackModel.Track

data class SearchItem(
    val type: Int,
    val album: Album?,
    val artist: Artist?,
    val playlist: Playlist?,
    val track: Track?
)
