package com.example.bookbase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.bookbase.ui.BookBaseApp
import com.example.bookbase.ui.theme.BookBaseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookBaseTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    BookBaseApp()
                }
            }
        }
    }
}