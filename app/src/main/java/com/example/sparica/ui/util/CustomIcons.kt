package com.example.sparica.ui.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sparica.R

@Composable
fun ExchangeRateIcon() {
    Icon(
        painter = painterResource(R.drawable.exchange_icon_white),
        contentDescription = "Exchange rates",
        tint = Color.White
    )
}

@Composable
fun NavigateBackIconButton(onClick:()->Unit) {
    IconButton(onClick = onClick) {
        Icon(Icons.Filled.ArrowBack, "Go back")
    }
}