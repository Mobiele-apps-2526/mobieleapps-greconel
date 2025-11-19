package com.example.bookbase.repositories

import com.example.bookbase.models.Book
import com.example.bookbase.services.BookApi

class BookRepository : IBookRepository {
    override suspend fun searchBooks(query: String): List<Book> {
        val response = BookApi.retrofitService.searchBooks(query)
        return response.items ?: emptyList()
    }

    override suspend fun getBook(bookId: String): Book {
        return BookApi.retrofitService.getBook(bookId)
    }
}