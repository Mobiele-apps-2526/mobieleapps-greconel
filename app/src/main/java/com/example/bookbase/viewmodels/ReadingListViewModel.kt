package com.example.bookbase.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookbase.models.Book
import com.example.bookbase.models.ReadingListBook
import com.example.bookbase.repositories.IBookRepository
import com.example.bookbase.repositories.IReadingListRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

sealed class ReadingListUiState {
    data class Success(val books: List<ReadingListBook>) : ReadingListUiState()
    data object Empty : ReadingListUiState()
    data object Loading : ReadingListUiState()
    data class Error(val message: String) : ReadingListUiState()
}

class ReadingListViewModel(
    private val readingListRepository: IReadingListRepository,
    private val bookRepository: IBookRepository
) : ViewModel() {

    var uiState: ReadingListUiState by mutableStateOf(ReadingListUiState.Loading)
        private set

    init {
        loadReadingList()
    }

    private fun loadReadingList() {
        viewModelScope.launch {
            uiState = ReadingListUiState.Loading

            readingListRepository.getAllBooks()
                .catch { e ->
                    uiState = ReadingListUiState.Error(e.message ?: "Onbekende fout")
                }
                .collect { books ->
                    uiState = if (books.isEmpty()) {
                        ReadingListUiState.Empty
                    } else {
                        ReadingListUiState.Success(books)
                    }
                }
        }
    }

    fun removeFromReadingList(book: ReadingListBook) {
        viewModelScope.launch {
            try {
                readingListRepository.removeBook(book)
            } catch (e: Exception) {
                uiState = ReadingListUiState.Error("Kon boek niet verwijderen: ${e.message}")
            }
        }
    }
}