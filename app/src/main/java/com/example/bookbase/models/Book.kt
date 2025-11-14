package com.example.bookbase.models

import kotlinx.serialization.Serializable

@Serializable
data class Book (
    val id: Int,
    val title: String,
    val authors: List<String> = emptyList(),
    val publishedDate: String? = null,
    val description: String? = null,
    val thumbnail: String?
)