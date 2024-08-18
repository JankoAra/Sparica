package com.example.sparica.ui.spendings.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.sparica.data.models.Currency
import com.example.sparica.data.models.Spending
import com.example.sparica.navigation.SpendingDetailsRoute
import com.example.sparica.ui.util.MyTopAppBar
import com.example.sparica.ui.util.NavigateBackIconButton
import com.example.sparica.ui.util.SwipeToDeleteContainer
import com.example.sparica.viewmodels.BudgetViewModel
import com.example.sparica.viewmodels.SpendingViewModel

@Composable
fun AllSpendingsForBudgetScreen(
    navController: NavController,
    spendingViewModel: SpendingViewModel,
    budgetViewModel: BudgetViewModel,
    modifier: Modifier = Modifier
) {
    val spendings by spendingViewModel.allSpendings.collectAsStateWithLifecycle()
    val activeBudget by budgetViewModel.activeBudget.collectAsStateWithLifecycle()
    var selectedDisplayCurrency by rememberSaveable {
        mutableStateOf(activeBudget?.defaultCurrency ?: Currency.RSD)
    }

    var totalSpending by rememberSaveable {
        mutableStateOf(Spending(price = 0.0, description = ""))
    }
    val totalPrice = spendings.map {
        it.copy(price = budgetViewModel.convert(it, selectedDisplayCurrency).price, currency = selectedDisplayCurrency)
    }.sumOf { it.price }

    LaunchedEffect(selectedDisplayCurrency, spendings, activeBudget) {
        totalSpending = Spending(price = totalPrice, currency = selectedDisplayCurrency, description = "")
    }
    Scaffold(
        topBar = {
            MyTopAppBar(
                titleLabel = activeBudget?.name ?: "",
                navigationIcon = {
                    NavigateBackIconButton {
                        navController.popBackStack()
                    }
                }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
        ) {
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "All spendings",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    CurrencyPickerDropdown(
                        onCurrencySelected = {
                            selectedDisplayCurrency = it
                        },
                        currencies = Currency.entries,
                        selectedCurrencyState = selectedDisplayCurrency,
                        label = "Choose display currency",
                        modifier = Modifier.padding(start = 4.dp)
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

            items(spendings, key = { it.id }) { spending ->
                SwipeToDeleteContainer(
                    item = spending,
                    onDelete = { spendingViewModel.markDeleted(spending) }) {
                    SpendingListItem(
                        spending = spending,
                        budgetViewModel = budgetViewModel,
                        targetCurrency = selectedDisplayCurrency
                    ) { navController.navigate(SpendingDetailsRoute(spending)) }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}