package com.example.sparica.ui.spendings.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.sparica.SparicaApp
import com.example.sparica.data.models.Currency
import com.example.sparica.data.models.Spending
import com.example.sparica.navigation.BudgetsMainScreenRoute
import com.example.sparica.navigation.ExchangeRateTableRoute
import com.example.sparica.navigation.SpendingDetailsRoute
import com.example.sparica.reporting.ReportUtils
import com.example.sparica.reporting.spendingsToCSV
import com.example.sparica.ui.util.MyTopAppBar
import com.example.sparica.viewmodels.BudgetViewModel
import com.example.sparica.viewmodels.SpendingViewModel
import java.time.LocalDateTime


@Composable
fun SingleBudgetScreen(
    navController: NavHostController,
    spendingViewModel: SpendingViewModel = viewModel(),
    budgetID: Int
) {
    spendingViewModel.getSpendingsForBudget(budgetID)

    val budgetViewModel = viewModel<BudgetViewModel>()
    val allBudgets by budgetViewModel.allBudgets.collectAsState(emptyList())

    // Collect spendings data as a state
    val spendings by spendingViewModel.allSpendings.collectAsState(emptyList())

    // State to hold the converted total
    var selectedCurrency by rememberSaveable { mutableStateOf<Currency>(Currency.RSD) }
    var totalPrice by rememberSaveable {
        mutableDoubleStateOf(0.0)
    }
    var totalSpending by rememberSaveable {
        mutableStateOf(
            Spending(
                description = "",
                currency = selectedCurrency,
                price = totalPrice
            )
        )
    }

    val app = LocalContext.current.applicationContext as SparicaApp

    // Perform the conversion in a coroutine
    LaunchedEffect(spendings, selectedCurrency) {
        //println("TOTAL PRICE1: $totalPrice")
        totalPrice = spendings.map { spending ->
            spendingViewModel.convertPrice(spending, selectedCurrency)
        }.sumOf { it.price }
        //println("TOTAL PRICE2: $totalPrice")
        totalSpending = totalSpending.copy(price = totalPrice, currency = selectedCurrency)
        //println(totalSpending.toString())
    }



    Scaffold(
        topBar = {
            MyTopAppBar(
                onGoBack = {},
                showBackButton = false,
                goToExchangeRate = {
                    spendingViewModel.getLatestExchangeRates()
                    navController.navigate(ExchangeRateTableRoute)
                })
        }
    ) { innerPadding ->
        // Use LazyColumn for the entire scrollable content
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp) // Add horizontal padding to content
        ) {

            item {
                Button(onClick = {
                    val timestamp =
                        LocalDateTime.now().noSpaces()
                    ReportUtils.createFile("report$timestamp.csv", spendingsToCSV(spendings))
                }) {
                    Text(text = "Generate CSV")
                }
                Button(onClick = {
                    val timestamp =
                        LocalDateTime.now().noSpaces()
                    ReportUtils.createFile("report$timestamp.pdf", spendingsToCSV(spendings))
                }) {
                    Text(text = "Generate PDF")
                }
            }
            // Insert the form as the first item
            item {
                InsertSpendingForm(spendingViewModel, budgetID)
                Spacer(modifier = Modifier.height(16.dp))
            }
            // Insert the title for spendings
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Spendings", style = MaterialTheme.typography.headlineSmall
                    )
                    CurrencyPickerDropdown(
                        onCurrencySelected = {
                            selectedCurrency = it
                        },
                        currencies = Currency.entries,
                        selectedCurrencyState = selectedCurrency,
                        label = "Choose display currency"
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "Total: ${totalSpending.getFormatedPrice()}",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Insert the list of spendings
            items(spendings, key = { it.id }) { spending ->
                SpendingListItem(spending = spending, spendingViewModel, selectedCurrency) {
                    navController.navigate(
                        SpendingDetailsRoute(spending)
                    ).also {
                        //println(spending.toString())
                        for (s in spendings) {
                            //println(s.id)
                        }
                    }
                }
            }

            item {
                Button(onClick = {
                    budgetViewModel.deleteBudget(allBudgets[allBudgets.indexOfFirst { it.id == budgetID }])
                    navController.navigate(BudgetsMainScreenRoute)
                }) {
                    Text(text = "Delete budget")
                }
            }
        }
    }


}

private fun LocalDateTime.noSpaces(): String {
    val string = "$year$monthValue$dayOfMonth$hour$minute$second"
    //println("TimeStamp no spaces: $string")
    return string
}


