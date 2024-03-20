package hr.ivuksan.sherry.viewModel.searchVM

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.ivuksan.sherry.ServiceGenerator
import hr.ivuksan.sherry.SpotifyApiInterface
import hr.ivuksan.sherry.model.searchModel.SearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel(application: Application): ViewModel() {

    var searchResponse = MutableLiveData<SearchResponse>()
    var tracksResponse = MutableLiveData<SearchResponse>()
    var errorLiveData = MutableLiveData<String>()
    var sharedPreferences = application.getSharedPreferences("AuthorizationPreferences", Context.MODE_PRIVATE)
    var baseUrl = "https://api.spotify.com/"

    fun searchForItem(query: String){
        val types = "album,artist,playlist,track"
        val accessToken = getAccessToken()
        val client: SpotifyApiInterface = ServiceGenerator().createService(
            SpotifyApiInterface::class.java,
            baseUrl
        )
        val call = client.searchForItem("Bearer $accessToken", query, types, 10)
        call.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>){
                if (response.isSuccessful){
                    val searches = response.body()
                    searchResponse.value = searches
                }else{
                    errorLiveData.value = "Error: ${response.code()}"
                }
            }
            override fun onFailure(call: Call<SearchResponse>, t: Throwable){
                errorLiveData.value = "Network request failed: ${t.message}"
            }
        })
    }

    fun searchForTracks(query: String){
        val types = "track"
        val accessToken = getAccessToken()
        val client: SpotifyApiInterface = ServiceGenerator().createService(
            SpotifyApiInterface::class.java,
            baseUrl
        )
        val call = client.searchForTrack("Bearer $accessToken", query, types, 20)
        call.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>){
                if (response.isSuccessful){
                    val searches = response.body()
                    tracksResponse.value = searches
                }else{
                    errorLiveData.value = "Error: ${response.code()}"
                }
            }
            override fun onFailure(call: Call<SearchResponse>, t: Throwable){
                errorLiveData.value = "Network request failed: ${t.message}"
            }
        })
    }

    private fun getAccessToken(): String {
        return sharedPreferences.getString("access_token", "") ?: ""
    }
}