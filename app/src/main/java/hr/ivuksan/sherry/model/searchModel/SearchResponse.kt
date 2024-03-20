package hr.ivuksan.sherry.model.searchModel

import hr.ivuksan.sherry.model.albumModel.AlbumList
import hr.ivuksan.sherry.model.artistModel.ArtistInfo
import hr.ivuksan.sherry.model.playlistModel.PlaylistList
import hr.ivuksan.sherry.model.trackModel.TrackInfo

data class SearchResponse(
    val albums: AlbumList,
    val artists: ArtistInfo,
    val playlists: PlaylistList,
    val tracks: TrackInfo
)