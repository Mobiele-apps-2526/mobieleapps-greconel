package com.example.bookbase.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reading_list")
data class ReadingListBook(
    @PrimaryKey
    val bookId: String,
    val title: String,
    val authors: String?, // Opgeslagen als comma-separated string
    val thumbnail: String?,
    val addedDate: Long = System.currentTimeMillis()
)

// Extension functie om een Book naar ReadingListBook te converteren
fun Book.toReadingListBook(): ReadingListBook {
    return ReadingListBook(
        bookId = this.id,
        title = this.volumeInfo.title,
        authors = this.volumeInfo.authors?.joinToString(", "),
        thumbnail = this.volumeInfo.imageLinks?.thumbnail
    )
}