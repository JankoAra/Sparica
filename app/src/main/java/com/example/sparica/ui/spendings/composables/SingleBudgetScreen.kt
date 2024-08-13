package com.example.sparica.ui.spendings.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.sparica.data.models.Currency
import com.example.sparica.data.models.Spending
import com.example.sparica.navigation.BudgetsMainScreenRoute
import com.example.sparica.navigation.ExchangeRateTableRoute
import com.example.sparica.navigation.SpendingDetailsRoute
import com.example.sparica.reporting.ReportUtils
import com.example.sparica.reporting.spendingsToCSV
import com.example.sparica.ui.util.ExchangeRateIcon
import com.example.sparica.ui.util.MyTopAppBar
import com.example.sparica.ui.util.NavigateBackIconButton
import com.example.sparica.ui.util.SwipeToDeleteContainer
import com.example.sparica.viewmodels.BudgetViewModel
import com.example.sparica.viewmodels.SpendingViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


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
    var selectedCurrency by rememberSaveable { mutableStateOf(Currency.RSD) }
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
    val budgetName = remember {
        mutableStateOf("")
    }
    LaunchedEffect(allBudgets) {
        val indexOfBudget = allBudgets.indexOfFirst { it.id == budgetID }
        if (indexOfBudget < 0) {
            budgetName.value = ""
        } else {
            budgetName.value = allBudgets[indexOfBudget!!].name
        }
    }

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
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(250.dp)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            scope.launch { drawerState.close() }
                        })
                    },

                ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Options",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(16.dp)
                    )

                    Divider()

                    TextButton(
                        onClick = {
                            val timestamp = LocalDateTime.now().noSpaces()
                            ReportUtils.createFile(
                                "report$timestamp.csv",
                                spendingsToCSV(spendings)
                            )
                            scope.launch { drawerState.close() }
                        },
                    ) {
                        Text("Save as CSV")
                    }

                    TextButton(
                        onClick = {
                            val timestamp = LocalDateTime.now().noSpaces()
                            ReportUtils.createFile(
                                "report$timestamp.pdf",
                                spendingsToCSV(spendings)
                            )
                            scope.launch { drawerState.close() }
                        },
                    ) {
                        Text("Save as PDF")
                    }

                    Spacer(modifier = Modifier.weight(1f)) // Pushes the content above to the top and the content below to the bottom

                    TextButton(
                        onClick = {
                            budgetViewModel.deleteBudget(allBudgets[allBudgets.indexOfFirst { it.id == budgetID }])
                            navController.navigate(BudgetsMainScreenRoute)
                            scope.launch { drawerState.close() }
                        }
                    ) {
                        Text("Delete budget", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                MyTopAppBar(
                    titleLabel = budgetName.value,
                    actions = {
                        val expanded = remember { mutableStateOf(false) }
                        DropdownMenu(
                            expanded = expanded.value,
                            onDismissRequest = { expanded.value = false },
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            DropdownMenuItem(onClick = {
                                // Handle menu item click
                                val timestamp =
                                    LocalDateTime.now().noSpaces()
                                ReportUtils.createFile(
                                    "report$timestamp.csv",
                                    spendingsToCSV(spendings)
                                )
                                expanded.value = false
                            }, text = {
                                Text("Save as CSV")
                            })
                            DropdownMenuItem(onClick = {
                                // Handle menu item click
                                val timestamp =
                                    LocalDateTime.now().noSpaces()
                                ReportUtils.createFile(
                                    "report$timestamp.pdf",
                                    spendingsToCSV(spendings)
                                )
                                expanded.value = false
                            }, text = {
                                Text("Save as PDF")
                            })
                            Divider(
                                modifier = Modifier.fillMaxWidth(),
                                thickness = 1.dp
                            )
                            DropdownMenuItem(onClick = {
                                // Handle menu item click
                                budgetViewModel.deleteBudget(allBudgets[allBudgets.indexOfFirst { it.id == budgetID }])
                                navController.navigate(BudgetsMainScreenRoute)
                                expanded.value = false
                            }, text = {
                                Text("Delete budget")
                            })
                        }
                        IconButton(onClick = { navController.navigate(ExchangeRateTableRoute) }) {
                            ExchangeRateIcon()
                        }
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    navigationIcon = {
                        NavigateBackIconButton {
                            navController.popBackStack()
                        }
                    }
                )
            }
        ) { innerPadding ->
            // Use LazyColumn for the entire scrollable content
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp) // Add horizontal padding to content
            ) {
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
                    SwipeToDeleteContainer(
                        item = spending,
                        onDelete = { spendingViewModel.markDeleted(spending) }) {
                        SpendingListItem(
                            spending = spending,
                            spendingViewModel = spendingViewModel,
                            targetCurrency = selectedCurrency,
                            onTap = { navController.navigate(SpendingDetailsRoute(spending)) }
                        )
                    }
                }
            }
        }
    }
}

private fun LocalDateTime.noSpaces(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
    return this.format(formatter)
}


