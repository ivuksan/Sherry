package hr.ivuksan.sherry.model.jsonBodyModel

import com.google.gson.annotations.SerializedName

data class CreatePlaylistBody(
    @SerializedName("name")
    val name: String
)