package com.example.sparica.ui.util

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.sparica.R

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MyTopAppBar2(
//    titleLabel: String = "Sparica",
//    actions:@Composable ()->Unit = {},
//    navigationIcon:@Composable ()->Unit = {},
//    modifier: Modifier = Modifier
//) {
//    val expanded = remember { mutableStateOf(false) }
//
//    TopAppBar(
//        title = { Text(titleLabel) },
//        navigationIcon = {
//            if (showBackButton) {
//                IconButton(onClick = onGoBack) {
//                    Icon(Icons.Filled.ArrowBack, "Go back")
//                }
//            }
//        },
//        actions = {
//            DropdownMenu(
//                expanded = expanded.value,
//                onDismissRequest = { expanded.value = false },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                DropdownMenuItem(onClick = {
//                    // Handle menu item click
//                    expanded.value = false
//                }, text = {
//                    Text("Save as CSV")
//                })
//                DropdownMenuItem(onClick = {
//                    // Handle menu item click
//                    expanded.value = false
//                }, text = {
//                    Text("Save as PDF")
//                })
//            }
//            IconButton(onClick = goToExchangeRate) {
//                Icon(
//                    painter = painterResource(R.drawable.exchange_icon_white),
//                    contentDescription = "Exchange rates",
//                    tint = Color.White
//                )
//            }
//            IconButton(onClick = { expanded.value = !expanded.value }) {
//                Icon(Icons.Default.MoreVert, contentDescription = "More options")
//            }
//        },
//    )
//}
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