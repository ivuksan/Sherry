package hr.ivuksan.sherry.model.jsonBodyModel

import com.google.gson.annotations.SerializedName

data class AddTrackBody(
    @SerializedName("uris")
    val uris: List<String>
)