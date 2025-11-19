package com.example.bookbase.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.bookbase.ui.navigation.BookBaseNavigation

@Composable
fun BookBaseApp() {
    Scaffold { innerPadding ->
        BookBaseNavigation(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}