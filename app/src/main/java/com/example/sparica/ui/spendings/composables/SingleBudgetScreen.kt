package com.example.sparica.ui.spendings.composables

import android.util.Log
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.sparica.data.models.Currency
import com.example.sparica.data.models.Spending
import com.example.sparica.data.models.SpendingCategory
import com.example.sparica.data.models.SpendingSubcategory
import com.example.sparica.data.query_objects.SpendingInfo
import com.example.sparica.data.query_objects.extractSpendingFromInfo
import com.example.sparica.navigation.AllSpendingsForBudgetRoute
import com.example.sparica.navigation.BudgetsMainScreenRoute
import com.example.sparica.navigation.EditBudgetRoute
import com.example.sparica.navigation.ExchangeRateTableRoute
import com.example.sparica.navigation.SpendingDetailsRoute
import com.example.sparica.navigation.SpendingFullStatsRoute
import com.example.sparica.navigation.TrashCanRoute
import com.example.sparica.reporting.ReportUtils
import com.example.sparica.reporting.noSpaces
import com.example.sparica.reporting.spendingsToCSV
import com.example.sparica.ui.util.ExchangeRateIcon
import com.example.sparica.ui.util.MyTopAppBar
import com.example.sparica.ui.util.NavigateBackIconButton
import com.example.sparica.ui.util.SwipeToDeleteContainer
import com.example.sparica.viewmodels.BudgetViewModel
import com.example.sparica.viewmodels.SpendingViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime


@Composable
fun SingleBudgetScreen(
    navController: NavHostController,
    spendingViewModel: SpendingViewModel,
    budgetViewModel: BudgetViewModel,
    budgetID: Int
) {
    val activeBudget by budgetViewModel.activeBudget.collectAsStateWithLifecycle()

    // Collect spendings data as a state
    val spendings by spendingViewModel.allSpendings.collectAsStateWithLifecycle(emptyList())

    val spendingInfo by spendingViewModel.spendingInfo.collectAsStateWithLifecycle(emptyList())

    val categoryMap by spendingViewModel.subcategoryMap.collectAsStateWithLifecycle()


    // State to hold the converted total
    var selectedDisplayCurrency by rememberSaveable {
        mutableStateOf(
            activeBudget?.defaultCurrency ?: Currency.RSD
        )
    }
    val spentPerCategory = spendingPerCategory(
        spendingInfo,
        categoryMap.keys.toList(),
        selectedDisplayCurrency,
        convert = { info, c ->
            budgetViewModel.convert(extractSpendingFromInfo(info), c)
        }
    )
    val spentPerSubcategory = spendingPerSubcategory(
        spendingInfo, categoryMap, selectedDisplayCurrency, convert = { info, c ->
            budgetViewModel.convert(
                extractSpendingFromInfo(info), c
            )
        }
    )

    var budgetName by remember {
        mutableStateOf(activeBudget?.name ?: "")
    }
    LaunchedEffect(activeBudget) {
        selectedDisplayCurrency = activeBudget?.defaultCurrency ?: Currency.RSD
        budgetName = activeBudget?.name ?: ""
    }
    var totalSpending by rememberSaveable {
        mutableStateOf(
            Spending(
                description = "",
                currency = selectedDisplayCurrency,
                price = 0.0
            )
        )
    }
    // Perform the conversion in a coroutine
    val scope = rememberCoroutineScope()
    DisposableEffect(spendings, selectedDisplayCurrency) {
        val job = scope.launch {
            val totalPrice = spendings.map {
                budgetViewModel.convert(it, selectedDisplayCurrency)
            }.sumOf { it.price }
            totalSpending =
                totalSpending.copy(price = totalPrice, currency = selectedDisplayCurrency)
            Log.d("SingleBudgetScreen", "spendings: $spendings")
            Log.d(
                "SingleBudgetScreen",
                "New total spending calculated: ${totalSpending.price} ${totalSpending.currency}"
            )
            Log.d("SingleBudgetScreen", "New total spending: $totalSpending")
        }

        // Clean up when the effect leaves the composition
        onDispose {
            job.cancel()
        }
    }
    val drawerState = rememberDrawerState(DrawerValue.Closed)



    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
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
                                    spendingsToCSV(spendingInfo, activeBudget!!) { s, c ->
                                        budgetViewModel.convert(s, c)
                                    }
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
                                    spendingsToCSV(spendingInfo, activeBudget!!) { s, c ->
                                        budgetViewModel.convert(s, c)
                                    }
                                )
                                scope.launch { drawerState.close() }
                            },
                        ) {
                            Text("Save as PDF")
                        }
                        Divider()
                        TextButton(
                            onClick = {
                                navController.navigate(EditBudgetRoute)
                            },
                        ) {
                            Text("Edit budget")
                        }

                        Spacer(modifier = Modifier.weight(1f)) // Pushes the content above to the top and the content below to the bottom

                        TextButton(
                            onClick = {
                                //budgetViewModel.deleteBudget(allBudgets[allBudgets.indexOfFirst { it.id == budgetID }])

                                navController.navigate(BudgetsMainScreenRoute) {
                                    popUpTo(BudgetsMainScreenRoute) {
                                        inclusive = true
                                    }
                                }

                                scope.launch { drawerState.close() }
                                budgetViewModel.deleteBudget(activeBudget!!)
                            }
                        ) {
                            Text("Delete budget", color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Scaffold(
                    topBar = {
                        MyTopAppBar(
                            titleLabel = budgetName,
                            actions = {
                                IconButton(onClick = { navController.navigate(TrashCanRoute) }) {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = "Deleted spendings"
                                    )
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
//                        item{
//                            Button(onClick = {
//                                if(spendingInfo.isNotEmpty()){
//                                    spendingInfo.forEach {
//                                        Log.d("SpendingsInfo", it.toString())
//                                    }
//                                }else{
//                                    Log.d("SpendingsInfo", "info is empty")
//                                }
//
//                            }) {
//                                Text(text = "Print info")
//                            }
//                            Button(onClick = {
//                                if(spendings.isNotEmpty()){
//                                    spendings.forEach {
//                                        Log.d("Spendings", it.toString())
//                                    }
//                                }else{
//                                    Log.d("Spendings", "spendings is empty")
//                                }
//
//                            }) {
//                                Text(text = "Print spendings")
//                            }
//                        }

                        // Insert the form as the first item
                        item {
                            InsertSpendingForm(spendingViewModel, budgetViewModel, budgetID)
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
                                    text = "Spendings",
                                    style = MaterialTheme.typography.headlineSmall
                                )
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

                        // Insert the list of spendings
                        items(
                            spendingInfo.subList(0, minOf(spendingInfo.size, 3)),
                            key = { it.id }) { info ->
                            val spending = extractSpendingFromInfo(info)
                            SwipeToDeleteContainer(
                                item = spending,
                                onDelete = { spendingViewModel.markDeleted(spending) }) {
                                SpendingListItem(
                                    info = info,
                                    budgetViewModel = budgetViewModel,
                                    targetCurrency = selectedDisplayCurrency
                                ) {
                                    navController.navigate(SpendingDetailsRoute(info))
                                }
                            }
                        }

                        if (spendingInfo.size > 0) {
                            item {
                                TextButton(onClick = {
                                    navController.navigate(
                                        AllSpendingsForBudgetRoute
                                    )
                                }) {
                                    Text(text = "View all spendings")
                                }
                            }
                        }
                        item { Divider() }

                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = "Stats",
                                    style = MaterialTheme.typography.titleLarge,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
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
                            }
                        }
                        item { Divider() }
                        for (subcat in spentPerSubcategory.keys.toList()
                            .subList(0, minOf(5, spentPerSubcategory.keys.size))) {
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
                            }
                        }
                        item {
                            TextButton(onClick = { navController.navigate(SpendingFullStatsRoute) }) {
                                Text(text = "View full stats")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}


fun spendingPerCategory(
    spendings: List<SpendingInfo>,
    categories: List<SpendingCategory>,
    currency: Currency,
    convert: (SpendingInfo, Currency) -> Spending
): Map<SpendingCategory, Double> {
    return categories
        .associateWith { category ->
            spendings
                .filter { it.categoryName == category.name }
                .sumOf { convert(it, currency).price }
        }.toList().sortedByDescending { (_, v) -> v }.toMap()
}

fun spendingPerSubcategory(
    spendings: List<SpendingInfo>,
    categoryMap: Map<SpendingCategory, List<SpendingSubcategory>>,
    currency: Currency,
    convert: (SpendingInfo, Currency) -> Spending
): Map<SpendingSubcategory, Double> {
    val subcategories = categoryMap.flatMap { (_, v) -> v }
    return subcategories.associateWith { subcat ->
        spendings
            .filter { s -> s.subcategoryID == subcat.id }
            .map { s -> convert(s, currency) }
            .sumOf { it.price }
    }.toList().sortedByDescending { (_, v) -> v }.toMap()
}
