package hr.ivuksan.sherry.viewModel.trackVM

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.ivuksan.sherry.ServiceGenerator
import hr.ivuksan.sherry.SpotifyApiInterface
import hr.ivuksan.sherry.model.jsonBodyModel.AddTrackBody
import hr.ivuksan.sherry.model.StartResumePlaybackBody
import hr.ivuksan.sherry.model.deviceModel.DeviceList
import hr.ivuksan.sherry.model.trackModel.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrackViewModel(application: Application): ViewModel() {

    var track = MutableLiveData<Track>()
    var deviceList = MutableLiveData<DeviceList>()
    var errorLiveData = MutableLiveData<String>()
    var sharedPreferences = application.getSharedPreferences("AuthorizationPreferences", Context.MODE_PRIVATE)
    var baseUrl = "https://api.spotify.com/"

    fun getTrack(trackId: String){
        val accessToken = getAccessToken()
        val url = baseUrl + "v1/tracks/" + trackId
        val client: SpotifyApiInterface = ServiceGenerator().createService(
            SpotifyApiInterface::class.java,
            baseUrl
        )
        val call = client.getTrack("Bearer $accessToken", url)
        call.enqueue(object : Callback<Track> {
            override fun onResponse(call: Call<Track>, response: Response<Track>) {
                if (response.isSuccessful) {
                    val trackResponse = response.body()
                    track.value = trackResponse
                }
            }
            override fun onFailure(call: Call<Track>, t: Throwable) {
                errorLiveData.value = "Network request failed: ${t.message}"
            }
        })
    }

    fun playSong(trackId: String, deviceId: String){
        val accessToken = getAccessToken()
        val url = baseUrl + "v1/me/player/play"
        val trackUris = listOf("spotify:track:$trackId")
        val body = StartResumePlaybackBody(uris = trackUris)
        val client: SpotifyApiInterface = ServiceGenerator().createService(
            SpotifyApiInterface::class.java,
            baseUrl
        )

        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    client.startOrResumeTrack("Bearer $accessToken", url, deviceId, body)
                        .execute()
                }
            } catch (e: Exception) {
                Log.e("Play/resume track error", e.toString())
            }
        }
    }

    fun getAvailableDevices(){
        val accessToken = getAccessToken()
        val client: SpotifyApiInterface = ServiceGenerator().createService(
            SpotifyApiInterface::class.java,
            baseUrl
        )
        val call = client.getAvailableDevices("Bearer $accessToken")
        call.enqueue(object : Callback<DeviceList>{
            override fun onResponse(call: Call<DeviceList>, response: Response<DeviceList>) {
                if (response.isSuccessful) {
                    val devicesResponse = response.body()
                    deviceList.value = devicesResponse
                }
            }
            override fun onFailure(call: Call<DeviceList>, t: Throwable) {
                errorLiveData.value = "Network request failed: ${t.message}"
            }
        })
    }

    fun addTrackToPlaylist(playlistId: String, uri: String){
        val accessToken = getAccessToken()
        val url = baseUrl + "v1/playlists/$playlistId/tracks"
        val uris = listOf(uri)
        val body = AddTrackBody(uris)
        val client: SpotifyApiInterface = ServiceGenerator().createService(
            SpotifyApiInterface::class.java,
            baseUrl
        )
        viewModelScope.launch(Dispatchers.IO) {
            try {
                client.addTrackToPlaylist("Bearer $accessToken", url, playlistId, body)
                    .execute()
            } catch (e: Exception) {
                Log.e("Add track error", e.toString())
            }
        }
    }

    private fun getAccessToken(): String {
        return sharedPreferences.getString("access_token", "") ?: ""
    }
}