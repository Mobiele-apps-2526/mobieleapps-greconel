package com.example.bookbase.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BooksResponse(
    val items: List<Book>? = null
)

@Serializable
data class Book(
    val id: String,
    val volumeInfo: VolumeInfo
)

@Serializable
data class VolumeInfo(
    val title: String,
    val authors: List<String>? = null,
    val publisher: String? = null,
    val publishedDate: String? = null,
    val description: String? = null,
    val pageCount: Int? = null,
    val categories: List<String>? = null,
    val imageLinks: ImageLinks? = null,
    val language: String? = null,
    @SerialName("averageRating") val averageRating: Double? = null,
    @SerialName("ratingsCount") val ratingsCount: Int? = null
)

@Serializable
data class ImageLinks(
    val thumbnail: String? = null,
    val smallThumbnail: String? = null
)