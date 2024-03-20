package hr.ivuksan.sherry.viewModel.playlistVM

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.ivuksan.sherry.ServiceGenerator
import hr.ivuksan.sherry.SpotifyApiInterface
import hr.ivuksan.sherry.model.jsonBodyModel.CreatePlaylistBody
import hr.ivuksan.sherry.model.playlistModel.Playlist
import hr.ivuksan.sherry.model.Section
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class PlaylistsViewModel(application: Application): ViewModel() {

    var playlistList = MutableLiveData<Section.Playlists>()
    var featuredPlaylistsList = MutableLiveData<Section.FeaturedPlaylists>()
    var playlist = MutableLiveData<Playlist>()
    var errorLiveData = MutableLiveData<String>()
    var sharedPreferences = application.getSharedPreferences("AuthorizationPreferences", Context.MODE_PRIVATE)
    var baseUrl = "https://api.spotify.com/"

    fun getUsersPlaylists(){
        val accessToken = getAccessToken()
        val client: SpotifyApiInterface = ServiceGenerator().createService(
            SpotifyApiInterface::class.java,
            baseUrl
        )
        val call = client.getUsersPlaylists("Bearer $accessToken")
        call.enqueue(object : Callback<Section.Playlists> {
            override fun onResponse(call: Call<Section.Playlists>, response: Response<Section.Playlists>){
                if (response.isSuccessful) {
                    val playlists = response.body()
                    playlistList.value = playlists
                }else{
                    errorLiveData.value = "Error: ${response.code()}"
                }
            }
            override fun onFailure(call: Call<Section.Playlists>, t: Throwable) {
                errorLiveData.value = "Network request failed: ${t.message}"
            }
        })
    }

    fun getPlaylist(playlistId: String){
        val accessToken = getAccessToken()
        val url = baseUrl + "v1/playlists/" + playlistId
        val client: SpotifyApiInterface = ServiceGenerator().createService(
            SpotifyApiInterface::class.java,
            baseUrl
        )
        val call = client.getPlaylist("Bearer $accessToken", url)
        call.enqueue(object : Callback<Playlist> {
            override fun onResponse(call: Call<Playlist>, response: Response<Playlist>) {
                if (response.isSuccessful) {
                    val playlistResponse = response.body()
                    playlist.value = playlistResponse
                }
            }
            override fun onFailure(call: Call<Playlist>, t: Throwable) {
                errorLiveData.value = "Network request failed: ${t.message}"
            }
        })
    }

    fun getFeaturedPlaylists(){
        val accessToken = getAccessToken()
        val client: SpotifyApiInterface = ServiceGenerator().createService(
            SpotifyApiInterface::class.java,
            baseUrl
        )
        val call = client.getFeaturedPlaylists("Bearer $accessToken")
        call.enqueue(object : Callback<Section.FeaturedPlaylists>{
            override fun onResponse(call: Call<Section.FeaturedPlaylists>, response: Response<Section.FeaturedPlaylists>) {
                val featuredPlaylistsResponse = response.body()
                featuredPlaylistsList.value = featuredPlaylistsResponse
            }

            override fun onFailure(call: Call<Section.FeaturedPlaylists>, t: Throwable) {
                errorLiveData.value = "Network request failed: ${t.message}"
            }
        })
    }

    fun createPlaylist(id: String, name: String){
        val accessToken = getAccessToken()
        val url = baseUrl + "v1/users/$id/playlists"
        val body = CreatePlaylistBody(name = name)
        val client: SpotifyApiInterface = ServiceGenerator().createService(
            SpotifyApiInterface::class.java,
            baseUrl
        )
        viewModelScope.launch(Dispatchers.IO) {
            try {
                client.createPlaylist("Bearer $accessToken", url, id, body)
                    .execute()
            } catch (e: Exception) {
                Log.e("Create playlist error", e.toString())
            }
        }
    }

    private fun getAccessToken(): String {
        return sharedPreferences.getString("access_token", "") ?: ""
    }
}