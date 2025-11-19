package com.example.bookbase.ui

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookbase.models.Book
import com.example.bookbase.repositories.BookRepository
import com.example.bookbase.repositories.IBookRepository
import com.example.bookbase.ui.screens.HomeScreen
import com.example.bookbase.viewmodels.BooksViewModel

@Composable
fun BookBaseApp() {
    Scaffold { innerPadding ->
        val bookRepository: IBookRepository = BookRepository()
        val viewModel = viewModel<BooksViewModel> {
            BooksViewModel(bookRepository)
        }

        val context = LocalContext.current
        val onListItemClick = { book: Book ->
            Toast.makeText(
                context,
                book.volumeInfo.title,
                Toast.LENGTH_SHORT
            ).show()
        }

        HomeScreen(
            viewModel = viewModel,
            onListItemClick = onListItemClick,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}