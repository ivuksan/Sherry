package hr.ivuksan.sherry.model.artistModel

import hr.ivuksan.sherry.model.albumModel.AlbumCover

data class Artist(val id:String, val name: String, val images: List<AlbumCover>)