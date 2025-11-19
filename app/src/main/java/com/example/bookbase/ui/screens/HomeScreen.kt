package com.example.bookbase.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.bookbase.R
import com.example.bookbase.models.Book
import com.example.bookbase.models.BookCategory
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
            onClearClick = { viewModel.clearSearch() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        when (bookUiState) {
            is BookUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())
            is BookUiState.Success -> CategoryListScreen(
                categories = bookUiState.categories,
                onBookClick = onListItemClick,
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
    onClearClick: () -> Unit,
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
            label = { Text("Zoek boeken") },
            modifier = Modifier.weight(1f),
            singleLine = true,
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = onClearClick) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Wissen"
                        )
                    }
                }
            }
        )
        Button(onClick = onSearchClick) {
            Text("Zoek")
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
            text = "Kon boeken niet laden",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )
        Button(onClick = retryAction) {
            Text("Opnieuw proberen")
        }
    }
}

@Composable
fun CategoryListScreen(
    categories: List<BookCategory>,
    onBookClick: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(categories) { category ->
            if (category.books.isNotEmpty()) {
                BookCategoryRow(
                    category = category,
                    onBookClick = onBookClick
                )
            }
        }
    }
}

@Composable
fun BookCategoryRow(
    category: BookCategory,
    onBookClick: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(vertical = 12.dp)) {
        Text(
            text = category.title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(category.books) { book ->
                BookCardHorizontal(
                    book = book,
                    onBookClick = onBookClick
                )
            }
        }
    }
}

@Composable
fun BookCardHorizontal(
    book: Book,
    onBookClick: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = modifier
            .width(140.dp)
            .clickable { onBookClick(book) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            // Book cover image
            val imageUrl = book.volumeInfo.imageLinks?.thumbnail?.replace("http://", "https://")
            if (imageUrl != null) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Boekomslag",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(4.dp))
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Geen\nafbeelding",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = book.volumeInfo.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.height(40.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = book.volumeInfo.authors?.firstOrNull() ?: "Onbekende auteur",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookCardHorizontalPreview() {
    BookBaseTheme {
        BookCardHorizontal(
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
            onBookClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BookCategoryRowPreview() {
    BookBaseTheme {
        BookCategoryRow(
            category = BookCategory(
                title = "Bestsellers",
                query = "bestsellers",
                books = listOf(
                    Book(
                        id = "1",
                        volumeInfo = VolumeInfo(
                            title = "Book 1",
                            authors = listOf("Author 1"),
                            publisher = null,
                            publishedDate = "2023",
                            description = null,
                            pageCount = null,
                            categories = null,
                            imageLinks = null,
                            language = null,
                            averageRating = null,
                            ratingsCount = null
                        )
                    ),
                    Book(
                        id = "2",
                        volumeInfo = VolumeInfo(
                            title = "Book 2",
                            authors = listOf("Author 2"),
                            publisher = null,
                            publishedDate = "2023",
                            description = null,
                            pageCount = null,
                            categories = null,
                            imageLinks = null,
                            language = null,
                            averageRating = null,
                            ratingsCount = null
                        )
                    )
                )
            ),
            onBookClick = {}
        )
    }
}