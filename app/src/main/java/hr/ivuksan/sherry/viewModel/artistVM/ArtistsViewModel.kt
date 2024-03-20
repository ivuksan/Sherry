package hr.ivuksan.sherry.viewModel.artistVM

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.ivuksan.sherry.ServiceGenerator
import hr.ivuksan.sherry.SpotifyApiInterface
import hr.ivuksan.sherry.model.Section
import hr.ivuksan.sherry.model.artistModel.Artist
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArtistsViewModel(application: Application): ViewModel() {

    var artistList = MutableLiveData<Section.Artists>()
    var artistsAlbums = MutableLiveData<Section.ArtistsAlbums>()
    var errorLiveData = MutableLiveData<String>()
    var sharedPreferences = application.getSharedPreferences("AuthorizationPreferences", Context.MODE_PRIVATE)
    var baseUrl = "https://api.spotify.com/"

    fun getUsersFollowedArtists(){
        val accessToken = getAccessToken()
        val client: SpotifyApiInterface = ServiceGenerator().createService(
            SpotifyApiInterface::class.java,
            baseUrl
        )
        val call = client.getUsersFollowedArtists("Bearer $accessToken")
        call.enqueue(object : Callback<Section.Artists> {
            override fun onResponse(call: Call<Section.Artists>, response: Response<Section.Artists>){
                if (response.isSuccessful) {
                    val artists = response.body()
                    artistList.value = artists
                }else{
                    errorLiveData.value = "Error: ${response.code()}"
                }
            }
            override fun onFailure(call: Call<Section.Artists>, t: Throwable) {
                errorLiveData.value = "Network request failed: ${t.message}"
            }
        })
    }

    fun getArtistsAlbums(artist: Artist?){
        val accessToken = getAccessToken()
        val url = baseUrl + "v1/artists/" + artist?.id + "/albums?include_groups=album"
        val client: SpotifyApiInterface = ServiceGenerator().createService(
            SpotifyApiInterface::class.java,
            baseUrl
        )
        val call = client.getArtistsAlbums("Bearer $accessToken", url)
        call.enqueue(object : Callback<Section.ArtistsAlbums>{
            override fun onResponse(call: Call<Section.ArtistsAlbums>, response: Response<Section.ArtistsAlbums>) {
                val albumsResponse = response.body()
                artistsAlbums.value = albumsResponse
            }

            override fun onFailure(call: Call<Section.ArtistsAlbums>, t: Throwable) {
                errorLiveData.value = "Network request failed: ${t.message}"
            }
        })
    }

    private fun getAccessToken(): String {
        return sharedPreferences.getString("access_token", "") ?: ""
    }
}