package hr.ivuksan.sherry.model

import com.google.gson.annotations.SerializedName

data class StartResumePlaybackBody(
    @SerializedName("uris")
    val uris: List<String>
)