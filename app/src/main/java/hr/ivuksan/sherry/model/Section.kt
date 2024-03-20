package hr.ivuksan.sherry.model

import hr.ivuksan.sherry.model.albumModel.Album
import hr.ivuksan.sherry.model.albumModel.AlbumInfo
import hr.ivuksan.sherry.model.albumModel.AlbumList
import hr.ivuksan.sherry.model.artistModel.ArtistInfo
import hr.ivuksan.sherry.model.playlistModel.Playlist

sealed class Section {
    data class NewReleases(val albums: AlbumList?): Section()
    data class FeaturedPlaylists(val playlists: Playlists): Section()
    data class ArtistsAlbums(val items: List<Album>): Section()

    data class Playlists(val items: List<Playlist>): Section()
    data class Albums(val items: List<AlbumInfo>): Section()
    data class Artists(val artists: ArtistInfo?): Section()
}