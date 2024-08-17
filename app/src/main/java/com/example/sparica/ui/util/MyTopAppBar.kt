package com.example.sparica.ui.util

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    titleLabel: String = "Sparica",
    actions:@Composable ()->Unit = {},
    navigationIcon:@Composable ()->Unit = {},
    modifier: Modifier = Modifier
) {
    val expanded = remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text(titleLabel) },
        navigationIcon = navigationIcon,
        actions = {actions()}
    )
}