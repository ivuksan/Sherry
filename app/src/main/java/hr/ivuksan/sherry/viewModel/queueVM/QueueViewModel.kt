package hr.ivuksan.sherry.viewModel.queueVM

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.ivuksan.sherry.ServiceGenerator
import hr.ivuksan.sherry.SpotifyApiInterface
import hr.ivuksan.sherry.model.playlistModel.PlaylistInfo
import hr.ivuksan.sherry.model.trackModel.PlaylistTrackInfo
import hr.ivuksan.sherry.model.trackModel.RecommendationTracks
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QueueViewModel(application: Application): ViewModel() {

    var recentlyPlayedList = MutableLiveData<PlaylistInfo>()
    var recommendationTracks = MutableLiveData<RecommendationTracks>()
    var errorLiveData = MutableLiveData<String>()
    var sharedPreferences = application.getSharedPreferences("AuthorizationPreferences", Context.MODE_PRIVATE)
    var baseUrl = "https://api.spotify.com/"

    fun getRecentlyPlayedTracks() {
        val accessToken = getAccessToken()
        val client: SpotifyApiInterface = ServiceGenerator().createService(
            SpotifyApiInterface::class.java,
            baseUrl
        )
        val call = client.getRecentlyPlayedTracks("Bearer $accessToken", 3)
        call.enqueue(object : Callback<PlaylistInfo>{
            override fun onResponse(call: Call<PlaylistInfo>, response: Response<PlaylistInfo>) {
                if (response.isSuccessful) {
                    val playlistResponse = response.body()
                    recentlyPlayedList.value = playlistResponse
                }else{
                    errorLiveData.value = "Error: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<PlaylistInfo>, t: Throwable) {
                errorLiveData.value = "Network request failed: ${t.message}"
            }
        })
    }

    fun getRecommendations(recentlyPlayedTracks: List<PlaylistTrackInfo>) {
        val accessToken = getAccessToken()
        val client: SpotifyApiInterface = ServiceGenerator().createService(
            SpotifyApiInterface::class.java,
            baseUrl
        )
        val seedTracks = recentlyPlayedTracks.joinToString(",") {it.track.id}

        val call = client.getRecommendations("Bearer $accessToken", seedTracks, 20)
        call.enqueue(object : Callback<RecommendationTracks>{
            override fun onResponse(call: Call<RecommendationTracks>, response: Response<RecommendationTracks>) {
                if (response.isSuccessful) {
                    val recommendationResponse = response.body()
                    recommendationTracks.value = recommendationResponse
                }else{
                    errorLiveData.value = "Error: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<RecommendationTracks>, t: Throwable) {
                errorLiveData.value = "Network request failed: ${t.message}"
            }
        })
    }

    private fun getAccessToken(): String {
        return sharedPreferences.getString("access_token", "") ?: ""
    }
}