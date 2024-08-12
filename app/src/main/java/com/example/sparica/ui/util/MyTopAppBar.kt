package com.example.sparica.ui.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.sparica.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    modifier: Modifier = Modifier,
    titleLabel: String = "Sparica",
    onGoBack: () -> Unit,
    showBackButton: Boolean,
    goToExchangeRate: () -> Unit
) {
    TopAppBar(
        title = { Text(titleLabel) },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = onGoBack) {
                    Icon(Icons.Filled.ArrowBack, "Go back")
                }
            }
        },
        actions = {
//            IconButton(onClick = goToExchangeRate) {
//                Icon(imageVector = Icons.Filled.DateRange, contentDescription ="Exchange rates")
//            }
            IconButton(onClick = goToExchangeRate) {
                Icon(
                    painter = painterResource(R.drawable.exchange_icon_white),
                    contentDescription = "Exchange rates",
                    tint = Color.White
                )
            }
        },
    )
}