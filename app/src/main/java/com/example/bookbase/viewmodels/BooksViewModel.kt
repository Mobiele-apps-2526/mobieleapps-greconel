package com.example.bookbase.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookbase.models.Book
import com.example.bookbase.models.BookCategory
import com.example.bookbase.repositories.IBookRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class BookUiState {
    data class Success(val categories: List<BookCategory>) : BookUiState()
    data object Error : BookUiState()
    data object Loading : BookUiState()
}

class BooksViewModel(private val bookRepository: IBookRepository) : ViewModel() {
    var bookUiState: BookUiState by mutableStateOf(BookUiState.Loading)
        private set

    var searchQuery by mutableStateOf("")
        private set

    // Definieer de categorieën die je wilt tonen
    private val categories = listOf(
        BookCategory("Nieuw uitgebracht", "new releases"),
        BookCategory("Bestsellers", "bestsellers"),
        BookCategory("BookTok Favorieten", "booktok"),
        BookCategory("Romantiek", "romance"),
        BookCategory("Thriller & Spanning", "thriller"),
        BookCategory("Fantasy", "fantasy"),
        BookCategory("Sci-Fi", "science fiction"),
        BookCategory("Young Adult", "young adult")
    )

    init {
        // Laad alle categorieën bij het opstarten
        loadAllCategories()
    }

    fun updateSearchQuery(query: String) {
        searchQuery = query
    }

    private fun loadAllCategories() {
        viewModelScope.launch {
            bookUiState = BookUiState.Loading
            bookUiState = try {
                // Laad alle categorieën parallel voor betere performance
                val loadedCategories = categories.map { category ->
                    async {
                        try {
                            val books = bookRepository.searchBooks(category.query)
                            category.copy(books = books)
                        } catch (e: Exception) {
                            category.copy(books = emptyList())
                        }
                    }
                }.map { it.await() }

                BookUiState.Success(loadedCategories)
            } catch (e: IOException) {
                BookUiState.Error
            } catch (e: HttpException) {
                BookUiState.Error
            }
        }
    }

    fun searchBooks() {
        // Als er gezocht wordt, toon zoekresultaten in één categorie
        if (searchQuery.isBlank()) {
            loadAllCategories()
            return
        }

        viewModelScope.launch {
            bookUiState = BookUiState.Loading
            bookUiState = try {
                val books = bookRepository.searchBooks(searchQuery)
                val searchCategory = BookCategory("Zoekresultaten", searchQuery, books)
                BookUiState.Success(listOf(searchCategory))
            } catch (e: IOException) {
                BookUiState.Error
            } catch (e: HttpException) {
                BookUiState.Error
            }
        }
    }

    fun clearSearch() {
        searchQuery = ""
        loadAllCategories()
    }
}