package com.example.bookbase.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookbase.models.Book
import com.example.bookbase.repositories.IBookRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class BookUiState {
    data class Success(val books: List<Book>) : BookUiState()
    data object Error : BookUiState()
    data object Loading : BookUiState()
}

class BooksViewModel(private val bookRepository: IBookRepository) : ViewModel() {
    var bookUiState: BookUiState by mutableStateOf(BookUiState.Loading)
        private set

    var searchQuery by mutableStateOf("")
        private set

    init {
        // Laad standaard boeken bij het opstarten
        loadInitialBooks()
    }

    fun updateSearchQuery(query: String) {
        searchQuery = query
    }

    private fun loadInitialBooks() {
        viewModelScope.launch {
            bookUiState = BookUiState.Loading
            bookUiState = try {
                // Laad populaire of bestseller boeken bij het opstarten
                val books = bookRepository.searchBooks("bestsellers")
                BookUiState.Success(books)
            } catch (e: IOException) {
                BookUiState.Error
            } catch (e: HttpException) {
                BookUiState.Error
            }
        }
    }

    fun searchBooks() {
        // Alleen zoeken als er een zoekopdracht is ingevoerd
        if (searchQuery.isBlank()) {
            loadInitialBooks()
            return
        }

        viewModelScope.launch {
            bookUiState = BookUiState.Loading
            bookUiState = try {
                val books = bookRepository.searchBooks(searchQuery)
                BookUiState.Success(books)
            } catch (e: IOException) {
                BookUiState.Error
            } catch (e: HttpException) {
                BookUiState.Error
            }
        }
    }
}