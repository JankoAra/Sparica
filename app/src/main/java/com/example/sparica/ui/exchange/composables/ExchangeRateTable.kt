package com.example.sparica.ui.exchange.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.sparica.data.models.Currency
import com.example.sparica.data.models.ExchangeRate
import com.example.sparica.ui.util.MyTopAppBar
import com.example.sparica.ui.util.NavigateBackIconButton
import com.example.sparica.viewmodels.BudgetViewModel
import com.example.sparica.viewmodels.SpendingViewModel
import java.time.LocalDate

@Composable
fun ExchangeRateTable(
    navHostController: NavHostController,
    budgetViewModel: BudgetViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        budgetViewModel.getLatestExchangeRates()
    }
    val rates by budgetViewModel.exchangeRates.collectAsState()
    val date = remember {
        mutableStateOf(LocalDate.now())
    }
    val sourceCurrencyCode = remember {
        mutableStateOf("")
    }
    var showAllRates by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(rates) {
        if (rates.isNotEmpty()) {
            date.value = rates[0].date
            sourceCurrencyCode.value = rates[0].baseCurrencyCode
        }
    }
    Scaffold(topBar = {
        MyTopAppBar(
            navigationIcon = {
                NavigateBackIconButton {
                    navHostController.popBackStack()
                }
            }
        )
    }) {
        //budgetViewModel.getLatestExchangeRates()
        LazyColumn(modifier = Modifier.padding(it)) {
            item {
                Text(text = "Date: " + date.value.toString())
                Text(text = "1 ${sourceCurrencyCode.value}=")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = showAllRates, onCheckedChange = { showAllRates = it })
                    Text(text = "Show all rates")
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            if (showAllRates) {
                itemsIndexed(
                    rates.distinct()
                ) { index, rate ->
                    //Text(text = index.toString())
                    ExchangeRateListEntry(entry = rate)
                }
            } else {
                itemsIndexed(
                    rates.distinct()
                        .filter { it.targetCurrencyCode in Currency.entries.map { it.name } }
                ) { index, rate ->
                    //Text(text = index.toString())
                    ExchangeRateListEntry(entry = rate)
                }
            }

        }
    }
}

@Composable
fun ExchangeRateListEntry(entry: ExchangeRate) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = entry.targetCurrencyCode)
        Spacer(modifier = Modifier.width(4.dp))
        DottedLine(
            modifier = Modifier
                .height(1.dp)
                .weight(1f)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = entry.exchangeRate.toString())
    }
}

@Composable
fun DottedLine(
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    dotRadius: Dp = 1.dp,
    dotSpacing: Dp = 10.dp
) {
    Canvas(modifier = modifier) {
        val totalWidth = size.width
        var currentX = 0f
        val radiusPx = dotRadius.toPx()
        val spacingPx = dotSpacing.toPx()

        while (currentX < totalWidth) {
            drawCircle(
                brush = SolidColor(color),
                radius = radiusPx,
                center = Offset(currentX + radiusPx, center.y)
            )
            currentX += 2 * radiusPx + spacingPx
        }
    }
}