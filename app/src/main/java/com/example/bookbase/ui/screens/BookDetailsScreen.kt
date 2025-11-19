package com.example.bookbase.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.bookbase.models.Book
import com.example.bookbase.viewmodels.BookDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailsScreen(
    viewModel: BookDetailsViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Boekdetails") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Terug"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        when {
            viewModel.isLoading -> {
                LoadingContent(modifier = Modifier.padding(innerPadding))
            }
            viewModel.error != null -> {
                ErrorContent(
                    error = viewModel.error!!,
                    modifier = Modifier.padding(innerPadding)
                )
            }
            viewModel.book != null -> {
                BookDetailsContent(
                    book = viewModel.book!!,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
fun LoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp)
        )
    }
}

@Composable
fun ErrorContent(
    error: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = error,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun BookDetailsContent(
    book: Book,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Header sectie met cover en basis info
        BookHeaderSection(book = book)

        // Details sectie
        BookDetailsSection(book = book)
    }
}

@Composable
fun BookHeaderSection(book: Book) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        // Book cover
        val imageUrl = book.volumeInfo.imageLinks?.thumbnail?.replace("http://", "https://")
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl.replace("zoom=1", "zoom=2"), // Hogere resolutie
                contentDescription = "Boekomslag van ${book.volumeInfo.title}",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(300.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        } else {
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .height(300.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Geen afbeelding\nbeschikbaar",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Titel
        Text(
            text = book.volumeInfo.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )

        // Auteur(s)
        book.volumeInfo.authors?.let { authors ->
            Text(
                text = authors.joinToString(", "),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        // Rating
        if (book.volumeInfo.averageRating != null) {
            RatingRow(
                rating = book.volumeInfo.averageRating,
                ratingsCount = book.volumeInfo.ratingsCount
            )
        }
    }
}

@Composable
fun RatingRow(
    rating: Double,
    ratingsCount: Int?,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = Color(0xFFFFB300),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = String.format("%.1f", rating),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        if (ratingsCount != null) {
            Text(
                text = " ($ratingsCount beoordelingen)",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun BookDetailsSection(book: Book) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        // Beschrijving
        book.volumeInfo.description?.let { description ->
            DetailCard(title = "Beschrijving") {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
                )
            }
        }

        // Publicatie informatie
        DetailCard(title = "Publicatie informatie") {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                book.volumeInfo.publisher?.let { publisher ->
                    InfoRow(label = "Uitgever", value = publisher)
                }
                book.volumeInfo.publishedDate?.let { date ->
                    InfoRow(label = "Publicatiedatum", value = date)
                }
                book.volumeInfo.pageCount?.let { pages ->
                    InfoRow(label = "Aantal pagina's", value = pages.toString())
                }
                book.volumeInfo.language?.let { language ->
                    val languageText = when (language) {
                        "en" -> "Engels"
                        "nl" -> "Nederlands"
                        "fr" -> "Frans"
                        "de" -> "Duits"
                        "es" -> "Spaans"
                        else -> language.uppercase()
                    }
                    InfoRow(label = "Taal", value = languageText)
                }
            }
        }

        // Categorieën
        book.volumeInfo.categories?.let { categories ->
            if (categories.isNotEmpty()) {
                DetailCard(title = "Categorieën") {
                    Text(
                        text = categories.joinToString(" • "),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun DetailCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            content()
        }
    }
}

@Composable
fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(2f)
        )
    }
}