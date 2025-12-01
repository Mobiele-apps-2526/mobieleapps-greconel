package com.example.bookbase.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookbase.repositories.BookRepository
import com.example.bookbase.repositories.IBookRepository
import com.example.bookbase.repositories.IReadingListRepository
import com.example.bookbase.repositories.ReadingListRepository
import com.example.bookbase.services.BookDatabase
import com.example.bookbase.ui.screens.BookDetailsScreen
import com.example.bookbase.ui.screens.HomeContent
import com.example.bookbase.ui.screens.ReadingListContent
import com.example.bookbase.ui.screens.SearchBar
import com.example.bookbase.viewmodels.BookDetailsViewModel
import com.example.bookbase.viewmodels.BooksViewModel
import com.example.bookbase.viewmodels.ReadingListViewModel

enum class BookBaseScreen {
    Home,
    ReadingList,
    Details
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookBaseApp() {
    val context = LocalContext.current
    val bookRepository: IBookRepository = BookRepository()
    val readingListRepository: IReadingListRepository = remember {
        ReadingListRepository(BookDatabase.getInstance(context).readingListDao())
    }

    // State voor navigatie
    var currentScreen by remember { mutableStateOf(BookBaseScreen.Home) }
    var selectedBookId by remember { mutableStateOf<String?>(null) }
    var selectedTab by remember { mutableIntStateOf(0) }

    // ViewModels
    val booksViewModel = viewModel<BooksViewModel> {
        BooksViewModel(bookRepository)
    }

    val readingListViewModel = viewModel<ReadingListViewModel> {
        ReadingListViewModel(readingListRepository, bookRepository)
    }

    if (currentScreen == BookBaseScreen.Details) {
        // Details scherm - volledig scherm
        selectedBookId?.let { bookId ->
            val detailsViewModel = viewModel<BookDetailsViewModel>(
                key = bookId
            ) {
                BookDetailsViewModel(bookRepository, readingListRepository, bookId)
            }

            BookDetailsScreen(
                viewModel = detailsViewModel,
                onNavigateBack = {
                    currentScreen = BookBaseScreen.Home
                    selectedBookId = null
                }
            )
        }
    } else {
        // Home/ReadingList scherm met tabs
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("BookBase") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Zoekbalk - alleen zichtbaar op Home tab
                if (selectedTab == 0) {
                    SearchBar(
                        searchQuery = booksViewModel.searchQuery,
                        onSearchQueryChange = { booksViewModel.updateSearchQuery(it) },
                        onSearchClick = { booksViewModel.searchBooks() },
                        onClearClick = { booksViewModel.clearSearch() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }

                // Tab Row
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Home") }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Leeslijst") }
                    )
                }

                // Content op basis van geselecteerde tab
                when (selectedTab) {
                    0 -> HomeContent(
                        viewModel = booksViewModel,
                        onListItemClick = { book ->
                            selectedBookId = book.id
                            currentScreen = BookBaseScreen.Details
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                    1 -> ReadingListContent(
                        viewModel = readingListViewModel,
                        onBookClick = { bookId ->
                            selectedBookId = bookId
                            currentScreen = BookBaseScreen.Details
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}