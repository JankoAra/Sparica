package com.example.sparica.ui.spendings.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.sparica.data.models.Currency
import com.example.sparica.data.models.Spending
import com.example.sparica.data.query_objects.extractSpendingFromInfo
import com.example.sparica.ui.util.MyTopAppBar
import com.example.sparica.ui.util.NavigateBackIconButton
import com.example.sparica.viewmodels.BudgetViewModel
import com.example.sparica.viewmodels.SpendingViewModel

@Composable
fun SpendingFullStatsScreen(
    navController: NavController,
    spendingViewModel: SpendingViewModel,
    budgetViewModel: BudgetViewModel,
    modifier: Modifier = Modifier
) {
    val activeBudget by budgetViewModel.activeBudget.collectAsStateWithLifecycle()

    // Collect spendings data as a state
    val spendings by spendingViewModel.allSpendings.collectAsStateWithLifecycle(emptyList())
    val info by spendingViewModel.spendingInfo.collectAsStateWithLifecycle(emptyList())

    val categoryMap by spendingViewModel.subcategoryMap.collectAsStateWithLifecycle()


    // State to hold the converted total
    var selectedDisplayCurrency by rememberSaveable {
        mutableStateOf(
            activeBudget?.defaultCurrency ?: Currency.RSD
        )
    }
    val spentPerCategory = spendingPerCategory(
        info,
        categoryMap.keys.toList(),
        selectedDisplayCurrency,
        convert = { s, c -> budgetViewModel.convert(extractSpendingFromInfo(s), c) }
    )
    val spentPerSubcategory = spendingPerSubcategory(
        info, categoryMap, selectedDisplayCurrency, convert = { s, c ->
            budgetViewModel.convert(
                extractSpendingFromInfo(s), c
            )
        }
    )

    Scaffold(
        topBar = {
            MyTopAppBar(
                navigationIcon = {
                    NavigateBackIconButton {
                        navController.popBackStack()
                    }
                },
                titleLabel = activeBudget?.name ?: ""
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it),
            contentPadding = PaddingValues(4.dp)
        ) {
            item {
                Text(
                    text = "Stats",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                CurrencyPickerDropdown(
                    selectedCurrencyState = selectedDisplayCurrency,
                    onCurrencySelected = { selectedDisplayCurrency = it },
                    currencies = Currency.entries
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            for (cat in spentPerCategory.keys) {
                item {
                    Row {
                        val temp = Spending(
                            price = spentPerCategory[cat]!!,
                            currency = selectedDisplayCurrency,
                            description = ""
                        )
                        Text(text = "${cat.name}: ${temp.getFormatedPrice()}")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            item {
                Divider()
                Spacer(modifier = Modifier.height(8.dp))
            }
            for (subcat in spentPerSubcategory.keys) {
                item {
                    Row {
                        val temp = Spending(
                            price = spentPerSubcategory[subcat]!!,
                            currency = selectedDisplayCurrency,
                            description = ""
                        )
                        val name = if (subcat.name.equals("etc")) {
                            val cat =
                                categoryMap.keys.first { it.id == subcat.categoryId }
                            "${cat.name} (etc)"
                        } else {
                            subcat.name
                        }
                        Text(text = "${name}: ${temp.getFormatedPrice()}")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

        }
    }
}