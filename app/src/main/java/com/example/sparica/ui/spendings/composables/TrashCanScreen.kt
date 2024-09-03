package com.example.sparica.ui.spendings.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.sparica.data.models.Spending
import com.example.sparica.data.query_objects.extractSpendingFromInfo
import com.example.sparica.ui.util.MyTopAppBar
import com.example.sparica.ui.util.NavigateBackIconButton
import com.example.sparica.viewmodels.BudgetViewModel
import com.example.sparica.viewmodels.SpendingViewModel
import kotlinx.coroutines.flow.filter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrashCanScreen(
    navController: NavController,
    spendingViewModel: SpendingViewModel,
    budgetViewModel: BudgetViewModel,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            MyTopAppBar(
                navigationIcon = {
                    NavigateBackIconButton {
                        navController.popBackStack()
                    }
                },
                titleLabel = "Trash can",
                actions = {}
            )
        },
    ) {
        val deletedSpendings by spendingViewModel.deletedSpendings.collectAsStateWithLifecycle()
        val deletedInfo by spendingViewModel.spendingInfoAll.collectAsStateWithLifecycle()
        var showDialog by remember {
            mutableStateOf(false)
        }
        var dialogSpending by remember {
            mutableStateOf<Spending?>(null)
        }
        LazyColumn(modifier = Modifier.padding(it)) {

            items(deletedInfo.filter { it.deleted }, key = { it.id }) { s ->
                SpendingListItem(
                    info = s,
                    budgetViewModel = budgetViewModel,
                    targetCurrency = s.currency,
                    onTap = {
                        showDialog = true
                        dialogSpending = extractSpendingFromInfo(s)
                        println("Delete items click, $s")
                    }
                )
//                Card(
//                    onClick = {
//                        showDialog = true
//                        dialogSpending = s
//                        println("Delete items click, $s")
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 8.dp, horizontal = 4.dp)
//                ) {
//                    Column(
//                        modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp)
//                    ) {
//                        Text(text = s.description)
//                        Text(text = s.getFormatedPrice())
//                    }
//                }
            }
            item {
                if (deletedSpendings.isEmpty()) {
                    Text(text = "Trash can is empty!")
                }
            }
            item {
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            showDialog = false
                            dialogSpending = null
                        },
                        confirmButton = {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Button(onClick = {
                                    dialogSpending!!.deleted = false
                                    dialogSpending!!.dateDeleted = null
                                    spendingViewModel.updateSpending(dialogSpending!!)
                                    showDialog = false
                                    dialogSpending = null
                                }) {
                                    Text(text = "Restore spending")
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                                Button(onClick = {
                                    spendingViewModel.delete(dialogSpending!!)
                                    showDialog = false
                                    dialogSpending = null
                                }) {
                                    Text(text = "Permanently delete spending")
                                }
                            }
                        },
                        text = {
                            Text(
                                text = "Deleted spending",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    )
                }
            }
        }
    }
}

