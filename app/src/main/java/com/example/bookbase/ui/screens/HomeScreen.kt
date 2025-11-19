package com.example.bookbase.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.bookbase.R
import com.example.bookbase.models.Book
import com.example.bookbase.models.ImageLinks
import com.example.bookbase.models.VolumeInfo
import com.example.bookbase.ui.theme.BookBaseTheme
import com.example.bookbase.viewmodels.BookUiState
import com.example.bookbase.viewmodels.BooksViewModel

@Composable
fun HomeScreen(
    viewModel: BooksViewModel,
    onListItemClick: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    val bookUiState = viewModel.bookUiState

    Column(modifier = modifier.fillMaxSize()) {
        SearchBar(
            searchQuery = viewModel.searchQuery,
            onSearchQueryChange = { viewModel.updateSearchQuery(it) },
            onSearchClick = { viewModel.searchBooks() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        when (bookUiState) {
            is BookUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())
            is BookUiState.Success -> BookListScreen(
                books = bookUiState.books,
                onListItemClick = onListItemClick,
                modifier = Modifier.fillMaxSize()
            )
            is BookUiState.Error -> ErrorScreen(
                retryAction = { viewModel.searchBooks() },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            label = { Text("Search books") },
            modifier = Modifier.weight(1f),
            singleLine = true
        )
        Button(onClick = onSearchClick) {
            Text("Search")
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp)
        )
    }
}

@Composable
fun ErrorScreen(
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Failed to load books",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )
        Button(onClick = retryAction) {
            Text("Retry")
        }
    }
}

@Composable
fun BookListScreen(
    books: List<Book>,
    onListItemClick: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(books) { book ->
            BookCard(
                book = book,
                onItemClick = onListItemClick
            )
        }
    }
}

@Composable
fun BookCard(
    book: Book,
    onItemClick: (Book) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable { onItemClick(book) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Book cover image
            val imageUrl = book.volumeInfo.imageLinks?.thumbnail?.replace("http://", "https://")
            if (imageUrl != null) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Book cover",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(width = 80.dp, height = 120.dp)
                        .clip(RoundedCornerShape(4.dp))
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(width = 80.dp, height = 120.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No\nImage",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = book.volumeInfo.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = book.volumeInfo.authors?.joinToString(", ") ?: "Unknown author",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = book.volumeInfo.publishedDate ?: "Unknown date",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookCardPreview() {
    BookBaseTheme {
        BookCard(
            book = Book(
                id = "1",
                volumeInfo = VolumeInfo(
                    title = "Android Programming: The Big Nerd Ranch Guide",
                    authors = listOf("Bill Phillips", "Chris Stewart"),
                    publisher = "Big Nerd Ranch",
                    publishedDate = "2019",
                    description = "A great book",
                    pageCount = 500,
                    categories = listOf("Programming"),
                    imageLinks = ImageLinks(thumbnail = null),
                    language = "en",
                    averageRating = 4.5,
                    ratingsCount = 100
                )
            ),
            onItemClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    BookBaseTheme {
        LoadingScreen()
    }
}