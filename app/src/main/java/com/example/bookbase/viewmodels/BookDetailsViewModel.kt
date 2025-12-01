package com.example.bookbase.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookbase.models.Book
import com.example.bookbase.models.toReadingListBook
import com.example.bookbase.repositories.IBookRepository
import com.example.bookbase.repositories.IReadingListRepository
import kotlinx.coroutines.launch

class BookDetailsViewModel(
    private val bookRepository: IBookRepository,
    private val readingListRepository: IReadingListRepository,
    bookId: String
) : ViewModel() {
    var book: Book? by mutableStateOf(null)
        private set

    var isLoading: Boolean by mutableStateOf(true)
        private set

    var error: String? by mutableStateOf(null)
        private set

    var isInReadingList: Boolean by mutableStateOf(false)
        private set

    var showSuccessMessage: Boolean by mutableStateOf(false)
        private set

    init {
        getBook(bookId)
        checkIfInReadingList(bookId)
    }

    private fun getBook(bookId: String) {
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

    private fun checkIfInReadingList(bookId: String) {
        viewModelScope.launch {
            isInReadingList = readingListRepository.isInReadingList(bookId)
        }
    }

    fun toggleReadingList() {
        viewModelScope.launch {
            book?.let { currentBook ->
                try {
                    if (isInReadingList) {
                        // Verwijder uit leeslijst
                        val readingListBook = readingListRepository.getBook(currentBook.id)
                        readingListBook?.let {
                            readingListRepository.removeBook(it)
                            isInReadingList = false
                        }
                    } else {
                        // Voeg toe aan leeslijst
                        readingListRepository.addBook(currentBook.toReadingListBook())
                        isInReadingList = true
                        showSuccessMessage = true

                        // Verberg bericht na 2 seconden
                        kotlinx.coroutines.delay(2000)
                        showSuccessMessage = false
                    }
                } catch (e: Exception) {
                    error = "Kon niet toevoegen aan leeslijst: ${e.message}"
                }
            }
        }
    }
}