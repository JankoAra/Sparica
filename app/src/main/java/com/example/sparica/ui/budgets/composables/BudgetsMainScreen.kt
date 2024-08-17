package com.example.sparica.ui.budgets.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sparica.data.models.Budget
import com.example.sparica.data.models.Currency
import com.example.sparica.navigation.BudgetDashboardRoute
import com.example.sparica.navigation.ExchangeRateTableRoute
import com.example.sparica.navigation.TrashCanRoute
import com.example.sparica.ui.spendings.composables.CurrencyPickerDropdown
import com.example.sparica.ui.util.ExchangeRateIcon
import com.example.sparica.ui.util.MyTopAppBar
import com.example.sparica.viewmodels.BudgetViewModel

@Composable
fun BudgetsMainScreen(
    navController: NavController,
    budgetViewModel: BudgetViewModel
) {
    val allBudgets by budgetViewModel.allBudgets.collectAsState(emptyList())
    var showNamingDialog by rememberSaveable { mutableStateOf(false) }
    var newBudgetName by rememberSaveable { mutableStateOf("") }
    Scaffold(
        topBar = {
            MyTopAppBar(
                actions = {
                    IconButton(onClick = { navController.navigate(TrashCanRoute) }) {
                        Icon(imageVector = Icons.Filled.Delete, contentDescription = "Deleted spendings")
                    }
                    IconButton(onClick = { navController.navigate(ExchangeRateTableRoute) }) {
                        ExchangeRateIcon()
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showNamingDialog = true
                },
                modifier = Modifier
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary, // Background color
                contentColor = Color.White // Icon color
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ) {
        if (allBudgets.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                Text(text = "No budgets are created. Create one now!")
            }
        } else {
            LazyColumn(modifier = Modifier.padding(it)) {
                items(allBudgets) {
                    BudgetListItem(budget = it) {
                        navController.navigate(BudgetDashboardRoute(it.id))
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
        if (showNamingDialog) {
            val focusRequester = remember { FocusRequester() }
            val focusManager = LocalFocusManager.current
            var selectedCurrency by remember { mutableStateOf(Currency.RSD) }
            LaunchedEffect(showNamingDialog) {
                focusRequester.requestFocus()
            }
            AlertDialog(
                onDismissRequest = {
                    showNamingDialog = false
                    focusManager.clearFocus()
                },
                title = { Text("Enter a Name") },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        OutlinedTextField(
                            value = newBudgetName,
                            onValueChange = { newBudgetName = it },
                            label = { Text("Name") },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                capitalization = KeyboardCapitalization.Sentences,
                                keyboardType = KeyboardType.Ascii
                            ),
                            singleLine = true,
                            modifier = Modifier.focusRequester(focusRequester)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        CurrencyPickerDropdown(
                            selectedCurrencyState = selectedCurrency,
                            onCurrencySelected = { selectedCurrency = it },
                            currencies = Currency.entries,
                            label = "Default currency",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            // Handle the confirmed name here
                            if (newBudgetName.isBlank()) newBudgetName = "Untitled"
                            val newBudget =
                                Budget(name = newBudgetName, defaultCurrency = selectedCurrency)
                            budgetViewModel.insertBudget(newBudget)

                            showNamingDialog = false
                            newBudgetName = ""
                            focusManager.clearFocus()
                        }
                    ) {
                        Text("Create")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showNamingDialog = false
                        newBudgetName = ""
                        focusManager.clearFocus()
                    }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }

}