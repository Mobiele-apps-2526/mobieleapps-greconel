package com.example.bookbase.services

import com.example.bookbase.models.Book
import com.example.bookbase.models.BooksResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://www.googleapis.com/books/v1/"

private val json = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
}

private val retrofit = Retrofit.Builder()
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface BookApiService {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 40
    ): BooksResponse

    @GET("volumes/{id}")
    suspend fun getBook(@Path("id") bookId: String): Book
}

object BookApi {
    val retrofitService: BookApiService by lazy {
        retrofit.create(BookApiService::class.java)
    }
}