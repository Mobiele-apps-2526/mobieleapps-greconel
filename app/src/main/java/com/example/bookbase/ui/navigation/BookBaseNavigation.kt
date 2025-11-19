package com.example.bookbase.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookbase.repositories.BookRepository
import com.example.bookbase.repositories.IBookRepository
import com.example.bookbase.ui.screens.BookDetailsScreen
import com.example.bookbase.ui.screens.HomeScreen
import com.example.bookbase.viewmodels.BookDetailsViewModel
import com.example.bookbase.viewmodels.BooksViewModel

enum class BookBaseScreen {
    Home,
    Details
}

@Composable
fun BookBaseNavigation(
    modifier: Modifier = Modifier
) {
    val bookRepository: IBookRepository = BookRepository()

    // State voor navigatie
    var currentScreen by remember { mutableStateOf(BookBaseScreen.Home) }
    var selectedBookId by remember { mutableStateOf<String?>(null) }

    when (currentScreen) {
        BookBaseScreen.Home -> {
            val viewModel = viewModel<BooksViewModel> {
                BooksViewModel(bookRepository)
            }

            HomeScreen(
                viewModel = viewModel,
                onListItemClick = { book ->
                    selectedBookId = book.id
                    currentScreen = BookBaseScreen.Details
                },
                modifier = modifier
            )
        }
        BookBaseScreen.Details -> {
            selectedBookId?.let { bookId ->
                val detailsViewModel = viewModel<BookDetailsViewModel>(
                    key = bookId
                ) {
                    BookDetailsViewModel(bookRepository, bookId)
                }

                BookDetailsScreen(
                    viewModel = detailsViewModel,
                    onNavigateBack = {
                        currentScreen = BookBaseScreen.Home
                        selectedBookId = null
                    },
                    modifier = modifier
                )
            }
        }
    }
}