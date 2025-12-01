package com.example.bookbase.services

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bookbase.models.ReadingListBook

@Database(entities = [ReadingListBook::class], version = 1, exportSchema = false)
abstract class BookDatabase : RoomDatabase() {

    abstract fun readingListDao(): ReadingListDao

    companion object {
        @Volatile
        private var INSTANCE: BookDatabase? = null

        fun getInstance(context: Context): BookDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookDatabase::class.java,
                    "book_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}