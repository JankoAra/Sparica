package com.example.sparica.ui.budgets.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.sparica.data.models.Budget
import com.example.sparica.data.models.Currency
import com.example.sparica.ui.spendings.composables.CurrencyPickerDropdown
import com.example.sparica.viewmodels.BudgetViewModel

@Composable
fun EditBudgetScreen(
    navController: NavController,
    budgetViewModel: BudgetViewModel,
    modifier: Modifier = Modifier
) {
    val budget by budgetViewModel.activeBudget.collectAsStateWithLifecycle()
    var name by rememberSaveable {
        mutableStateOf(budget!!.name)
    }
    var selectedCurrency by rememberSaveable {
        mutableStateOf(budget!!.defaultCurrency)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(text = "Budget name") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        CurrencyPickerDropdown(
            selectedCurrencyState = selectedCurrency,
            onCurrencySelected = { selectedCurrency = it },
            currencies = Currency.entries
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            budget!!.name = name
            budget!!.defaultCurrency = selectedCurrency
            budgetViewModel.updateBudget(budget!!)
            navController.popBackStack()
        }) {
            Text(text = "Save changes")
        }
    }
}