package com.example.bookbase.repositories

import com.example.bookbase.models.ReadingListBook
import com.example.bookbase.services.ReadingListDao
import kotlinx.coroutines.flow.Flow

interface IReadingListRepository {
    fun getAllBooks(): Flow<List<ReadingListBook>>
    suspend fun getBook(bookId: String): ReadingListBook?
    suspend fun addBook(book: ReadingListBook)
    suspend fun removeBook(book: ReadingListBook)
    suspend fun isInReadingList(bookId: String): Boolean
}

class ReadingListRepository(private val dao: ReadingListDao) : IReadingListRepository {

    override fun getAllBooks(): Flow<List<ReadingListBook>> {
        return dao.getAllBooks()
    }

    override suspend fun getBook(bookId: String): ReadingListBook? {
        return dao.getBook(bookId)
    }

    override suspend fun addBook(book: ReadingListBook) {
        dao.insert(book)
    }

    override suspend fun removeBook(book: ReadingListBook) {
        dao.delete(book)
    }

    override suspend fun isInReadingList(bookId: String): Boolean {
        return dao.isInReadingList(bookId)
    }
}