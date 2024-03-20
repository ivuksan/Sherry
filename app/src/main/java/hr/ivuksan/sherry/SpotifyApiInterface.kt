package hr.ivuksan.sherry

import hr.ivuksan.sherry.model.*
import hr.ivuksan.sherry.model.searchModel.SearchResponse
import hr.ivuksan.sherry.model.playlistModel.Playlist
import hr.ivuksan.sherry.model.albumModel.Album
import hr.ivuksan.sherry.model.deviceModel.DeviceList
import hr.ivuksan.sherry.model.jsonBodyModel.AddTrackBody
import hr.ivuksan.sherry.model.jsonBodyModel.CreatePlaylistBody
import hr.ivuksan.sherry.model.playlistModel.PlaylistInfo
import hr.ivuksan.sherry.model.trackModel.RecommendationTracks
import hr.ivuksan.sherry.model.trackModel.Track
import retrofit2.Call
import retrofit2.http.*

interface SpotifyApiInterface {

    @GET("v1/browse/featured-playlists?country=HR")
    fun getFeaturedPlaylists(
        @Header("Authorization") authorization: String
    ): Call<Section.FeaturedPlaylists>

    @GET("v1/me/playlists")
    fun getUsersPlaylists(
        @Header("Authorization") accessToken: String
    ): Call<Section.Playlists>

    @GET
    fun getPlaylist(
        @Header("Authorization") accessToken: String,
        @Url url: String
    ): Call<Playlist>

    @GET("v1/browse/new-releases?country=HR")
    fun getNewReleases(
        @Header("Authorization") authorization: String
    ): Call<Section.NewReleases>

    @GET("v1/me/albums")
    fun getUsersSavedAlbums(
        @Header("Authorization") accessToken: String
    ): Call<Section.Albums>

    @GET
    fun getAlbum(
        @Header("Authorization") accessToken: String,
        @Url url: String
    ): Call<Album>

    @GET
    fun getArtistsAlbums(
        @Header("Authorization") authorization: String,
        @Url url: String
    ): Call<Section.ArtistsAlbums>

    @GET("v1/me/following?type=artist")
    fun getUsersFollowedArtists(
        @Header("Authorization") accessToken: String
    ): Call<Section.Artists>

    @GET("v1/me/player/recently-played")
    fun getRecentlyPlayedTracks(
        @Header("Authorization") accessToken: String,
        @Query("limit") limit: Int
    ): Call<PlaylistInfo>

    @GET
    fun getTrack(
        @Header("Authorization") accessToken: String,
        @Url url: String
    ): Call<Track>

    @GET("v1/me/player/devices")
    fun getAvailableDevices(
        @Header("Authorization") accessToken: String
    ): Call<DeviceList>

    @PUT
    fun startOrResumeTrack(
        @Header("Authorization") accessToken: String,
        @Url url: String,
        @Query("device_id") device_id: String,
        @Body body: StartResumePlaybackBody
    ): Call<Void>

    @GET("v1/recommendations")
    fun getRecommendations(
        @Header("Authorization") authorization: String,
        @Query("seed_tracks") seedTracks: String,
        @Query("limit") limit: Int
    ): Call<RecommendationTracks>

    @GET("v1/search")
    fun searchForItem(
        @Header("Authorization") accessToken: String,
        @Query("q") query: String,
        @Query("type") type: String,
        @Query("limit") limit: Int
    ): Call<SearchResponse>

    @GET("v1/search")
    fun searchForTrack(
        @Header("Authorization") accessToken: String,
        @Query("q") query: String,
        @Query("type") type: String,
        @Query("limit") limit: Int
    ): Call<SearchResponse>

    @GET("v1/me")
    fun getUser(
        @Header("Authorization") accessToken: String
    ): Call<User>

    @POST
    fun createPlaylist(
        @Header("Authorization") accessToken: String,
        @Url url: String,
        @Query("user_id") user_id: String,
        @Body body: CreatePlaylistBody
    ): Call<Void>

    @POST
    fun addTrackToPlaylist(
        @Header("Authorization") accessToken: String,
        @Url url: String,
        @Query("playlist_id") playlist_id: String,
        @Body body: AddTrackBody
    ): Call<Void>
}