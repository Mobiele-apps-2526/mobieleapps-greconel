package com.example.bookbase.services

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bookbase.models.ReadingListBook
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadingListDao {

    @Query("SELECT * FROM reading_list ORDER BY addedDate DESC")
    fun getAllBooks(): Flow<List<ReadingListBook>>

    @Query("SELECT * FROM reading_list WHERE bookId = :bookId")
    suspend fun getBook(bookId: String): ReadingListBook?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(book: ReadingListBook)

    @Delete
    suspend fun delete(book: ReadingListBook)

    @Query("SELECT EXISTS(SELECT 1 FROM reading_list WHERE bookId = :bookId)")
    suspend fun isInReadingList(bookId: String): Boolean
}