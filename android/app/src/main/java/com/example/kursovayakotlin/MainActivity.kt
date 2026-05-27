package com.example.kursovayakotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.kursovayakotlin.presentation.navigation.AppNavigation
import com.example.kursovayakotlin.presentation.theme.KursovayaKotlinTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KursovayaKotlinTheme {
                AppNavigation()
            }
        }
    }
}
