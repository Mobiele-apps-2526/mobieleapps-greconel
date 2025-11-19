package com.example.bookbase.models

data class BookCategory(
    val title: String,
    val query: String,
    val books: List<Book> = emptyList()
)