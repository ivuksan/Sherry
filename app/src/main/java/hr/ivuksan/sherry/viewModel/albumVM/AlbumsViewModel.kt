package hr.ivuksan.sherry.viewModel.albumVM

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.ivuksan.sherry.ServiceGenerator
import hr.ivuksan.sherry.SpotifyApiInterface
import hr.ivuksan.sherry.model.Section
import hr.ivuksan.sherry.model.albumModel.Album
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlbumsViewModel(application: Application): ViewModel() {

    var albumList = MutableLiveData<Section.Albums>()
    var newReleasesList = MutableLiveData<Section.NewReleases>()
    var album = MutableLiveData<Album>()
    var errorLiveData = MutableLiveData<String>()
    var sharedPreferences = application.getSharedPreferences("AuthorizationPreferences", Context.MODE_PRIVATE)
    var baseUrl = "https://api.spotify.com/"

    fun getUsersSavedAlbums(){
        val accessToken = getAccessToken()
        val client: SpotifyApiInterface = ServiceGenerator().createService(
            SpotifyApiInterface::class.java,
            baseUrl
        )
        val call = client.getUsersSavedAlbums("Bearer $accessToken")
        call.enqueue(object : Callback<Section.Albums> {
            override fun onResponse(call: Call<Section.Albums>, response: Response<Section.Albums>){
                if (response.isSuccessful) {
                    val albumsResponse = response.body()
                    albumList.value = albumsResponse
                }else{
                    errorLiveData.value = "Error: ${response.code()}"
                }
            }
            override fun onFailure(call: Call<Section.Albums>, t: Throwable) {
                errorLiveData.value = "Network request failed: ${t.message}"
            }
        })
    }

    fun getAlbum(albumId: String){
        val accessToken = getAccessToken()
        val url = baseUrl + "v1/albums/" + albumId
        val client: SpotifyApiInterface = ServiceGenerator().createService(
            SpotifyApiInterface::class.java,
            baseUrl
        )
        val call = client.getAlbum("Bearer $accessToken", url)
        call.enqueue(object : Callback<Album> {
            override fun onResponse(call: Call<Album>, response: Response<Album>) {
                if (response.isSuccessful) {
                    val albumResponse = response.body()
                    album.value = albumResponse
                }
            }
            override fun onFailure(call: Call<Album>, t: Throwable) {
                errorLiveData.value = "Network request failed: ${t.message}"
            }
        })
    }

    fun getNewReleases(){
        val accessToken = getAccessToken()
        val client: SpotifyApiInterface = ServiceGenerator().createService(
            SpotifyApiInterface::class.java,
            baseUrl
        )
        val call = client.getNewReleases("Bearer $accessToken")
        call.enqueue(object : Callback<Section.NewReleases>{
            override fun onResponse(call: Call<Section.NewReleases>, response: Response<Section.NewReleases>) {
                val newReleasesResponse = response.body()
                newReleasesList.value = newReleasesResponse
            }
            override fun onFailure(call: Call<Section.NewReleases>, t: Throwable) {
                errorLiveData.value = "Network request failed: ${t.message}"
            }
        })
    }

    private fun getAccessToken(): String {
        return sharedPreferences.getString("access_token", "") ?: ""
    }
}