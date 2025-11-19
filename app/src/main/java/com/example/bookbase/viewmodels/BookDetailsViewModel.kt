package com.example.bookbase.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookbase.models.Book
import com.example.bookbase.repositories.IBookRepository
import kotlinx.coroutines.launch

class BookDetailsViewModel(
    private val bookRepository: IBookRepository,
    bookId: String
) : ViewModel() {
    var book: Book? by mutableStateOf(null)
        private set

    var isLoading: Boolean by mutableStateOf(true)
        private set

    var error: String? by mutableStateOf(null)
        private set

    init {
        getBook(bookId)
    }

    fun getBook(bookId: String) {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                book = bookRepository.getBook(bookId)
            } catch (e: Exception) {
                error = "Kon boekdetails niet laden: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}