package com.example.bookbase.repositories

import com.example.bookbase.models.Book

interface IBookRepository {
    suspend fun searchBooks(query: String): List<Book>
    suspend fun getBook(bookId: String): Book
}