package com.example.sparica.ui.exchange.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.sparica.ui.util.MyTopAppBar
import com.example.sparica.viewmodels.SpendingViewModel

@Composable
fun ExchangeRateTable(
    navHostController: NavHostController,
    spendingViewModel: SpendingViewModel = viewModel()
) {
    val rates by spendingViewModel.exchangeRates.collectAsState()
    Scaffold(topBar = {
        MyTopAppBar(
            onGoBack = { navHostController.popBackStack() },
            showBackButton = true,
            goToExchangeRate = {})
    }) {
        LazyColumn(modifier = Modifier.padding(it)) {
            items(rates) { rate ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(text = rate.toString())
                }
            }
        }
    }

}