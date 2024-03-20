package hr.ivuksan.sherry.model.playlistModel

import hr.ivuksan.sherry.model.albumModel.AlbumCover

data class Playlist(
    val id: String,
    val name: String,
    val images: List<AlbumCover>,
    val description: String,
    val tracks: PlaylistInfo
    )