package com.example.sparica.ui.spendings.composables

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sparica.data.models.Currency
import com.example.sparica.data.models.Spending
import com.example.sparica.data.models.SpendingSubcategory
import com.example.sparica.ui.util.MyTopAppBar
import com.example.sparica.ui.util.NavigateBackIconButton
import com.example.sparica.viewmodels.SpendingViewModel
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpendingDetailsScreen(
    spending: Spending,
    onClickBack: () -> Unit,
    updateSpending: (Spending) -> Unit,
    spendingViewModel: SpendingViewModel
) {
    Scaffold(topBar = {
        MyTopAppBar(
            navigationIcon = { NavigateBackIconButton(onClickBack) },
        )
    }) {
        val categories by spendingViewModel.allCategories.collectAsState(emptyList())
        val subcategoryMap by spendingViewModel.subcategoryMap.collectAsState(emptyMap())
        var showDialogEditDescription by rememberSaveable {
            mutableStateOf(false)
        }
        var showDialogEditPrice by rememberSaveable {
            mutableStateOf(false)
        }
        var showDialogEditCategory by rememberSaveable {
            mutableStateOf(false)
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Description: ")
                Text(text = spending.description)
                IconButton(onClick = { showDialogEditDescription = true }) {
                    Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit description")
                }
                if (showDialogEditDescription) {
                    var editedDescription by remember {
                        mutableStateOf(spending.description)
                    }
                    AlertDialog(
                        onDismissRequest = {
                            showDialogEditDescription = false
                            editedDescription = spending.description
                        },
                        confirmButton = {
                            Button(onClick = {
                                spending.description = editedDescription
                                updateSpending(spending)
                                showDialogEditDescription = false
                            }) {
                                Text(text = "Change description")
                            }
                        },
                        text = {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp)
                            ) {
                                Text(
                                    text = "Edit description",
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                OutlinedTextField(
                                    value = editedDescription,
                                    onValueChange = { editedDescription = it },
                                    label = { Text(text = "Description") }
                                )
                            }
                        }
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Price:")
                Text(text = spending.getFormatedPrice())
                IconButton(onClick = { showDialogEditPrice = true }) {
                    Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit price")
                }
                if (showDialogEditPrice) {
                    var editedPrice by remember {
                        mutableStateOf(spending.price.toString())
                    }
                    var editedCurrency by remember {
                        mutableStateOf(spending.currency)
                    }
                    AlertDialog(
                        onDismissRequest = {
                            showDialogEditPrice = false
                            editedPrice = spending.price.toString()
                            editedCurrency = spending.currency
                        },
                        confirmButton = {
                            Button(onClick = {
                                spending.price = editedPrice.toDoubleOrNull() ?: 0.0
                                spending.currency = editedCurrency
                                updateSpending(spending)
                                showDialogEditPrice = false
                            }) {
                                Text(text = "Change price")
                            }
                        },
                        text = {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp)
                            ) {
                                Text(
                                    text = "Edit price",
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    OutlinedTextField(
                                        value = editedPrice,
                                        onValueChange = { editedPrice = it },
                                        label = { Text(text = "Price") },
                                        keyboardOptions = KeyboardOptions.Default.copy(
                                            keyboardType = KeyboardType.Number
                                        ),
                                        modifier = Modifier.weight(1f)
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    CurrencyPickerDropdown(
                                        selectedCurrencyState = editedCurrency,
                                        onCurrencySelected = { editedCurrency = it },
                                        currencies = Currency.entries,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Category:")
                val subcatText = if (spending.subcategory == null) {
                    ""
                } else {
                    "(${spending.subcategory!!.name})"
                }
                Text(text = spending.category.toString() + subcatText)
                IconButton(onClick = { showDialogEditCategory = true }) {
                    Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit description")
                }
                if (showDialogEditCategory) {
                    var editedCategory by remember {
                        mutableStateOf(spending.category)
                    }
                    var editedSubcategory by remember {
                        mutableStateOf(spending.subcategory)
                    }
                    AlertDialog(
                        onDismissRequest = {
                            showDialogEditCategory = false
                            editedCategory = spending.category
                            editedSubcategory = spending.subcategory
                        },
                        confirmButton = {
                            Button(onClick = {
                                spending.category = editedCategory
                                spending.subcategory = editedSubcategory
                                updateSpending(spending)
                                showDialogEditCategory = false
                            }) {
                                Text(text = "Change category")
                            }
                        },
                        text = {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp)
                            ) {
                                Text(
                                    text = "Edit category",
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                RadioButtonGrid(
                                    options = categories,
                                    selectedOption = editedCategory,
                                    onOptionSelected = {
                                        editedCategory = it
                                        editedSubcategory = null
                                    },
                                    labelProvider = { it.name }
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                // Display subcategories for the selected category
                                editedCategory?.let { category ->
                                    val subcategories = subcategoryMap[category] ?: emptyList()
                                    if (subcategories.isEmpty()) {
                                        editedSubcategory = null
                                        return@let
                                    }
                                    if (editedSubcategory == null) {
                                        var etcIndex = subcategories.indexOf(
                                            SpendingSubcategory(
                                                name = "etc",
                                                categoryId = editedCategory!!.id,
                                                order = 0
                                            )
                                        )
                                        if (etcIndex == -1) {
                                            etcIndex = 0
                                        }
                                        editedSubcategory = subcategories[etcIndex]
                                    }
                                    Text(
                                        text = "Subcategory",
                                        style = MaterialTheme.typography.headlineSmall
                                    )
                                    RadioButtonGrid(
                                        options = subcategories,
                                        selectedOption = editedSubcategory,
                                        onOptionSelected = { editedSubcategory = it },
                                        labelProvider = { it.name }
                                    )
                                }
                            }
                        }
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                var showDialogEditDatetime by remember {
                    mutableStateOf(false)
                }
                Text(text = "Date and time:")
                val formatter = DateTimeFormatter.ofPattern("dd:MM:yyyy HH:mm")
                Text(text = spending.date.format(formatter))
                IconButton(onClick = { showDialogEditDatetime = true }) {
                    Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit date and time")
                }
                if (showDialogEditDatetime) {
                    var editedDate by remember {
                        mutableStateOf(spending.date)
                    }
                    // Date and Time picker dialogs
                    val context = LocalContext.current

                    // DatePickerDialog
                    val datePickerDialog = DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            editedDate = editedDate.withYear(year).withMonth(month + 1)
                                .withDayOfMonth(dayOfMonth)
                        },
                        editedDate.year,
                        editedDate.monthValue - 1,
                        editedDate.dayOfMonth
                    )

                    // TimePickerDialog
                    val timePickerDialog = TimePickerDialog(
                        context,
                        { _, hourOfDay, minute ->
                            editedDate = editedDate.withHour(hourOfDay).withMinute(minute)
                        },
                        editedDate.hour,
                        editedDate.minute,
                        true
                    )
                    AlertDialog(
                        onDismissRequest = {
                            showDialogEditDatetime = false
                            editedDate = spending.date
                        },
                        confirmButton = {
                            Button(onClick = {
                                spending.date = editedDate
                                updateSpending(spending)
                                showDialogEditDatetime = false
                            }) {
                                Text(text = "Change date and time")
                            }
                        },
                        text = {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp)
                            ) {
                                Text(
                                    text = "Edit date and time",
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                // Show the current selected date and time
                                Text(
                                    text = "Selected: ${
                                        editedDate.format(
                                            DateTimeFormatter.ofPattern(
                                                "dd:MM:yyyy HH:mm"
                                            )
                                        )
                                    }"
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                // Buttons to trigger DatePicker and TimePicker dialogs
                                Row {
                                    Button(onClick = { datePickerDialog.show() }) {
                                        Text(text = "Pick Date")
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(onClick = { timePickerDialog.show() }) {
                                        Text(text = "Pick Time")
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewSpendingDetailsScreen() {
//    val spending = Spending(0, "nacosi", 123.124, null, currency = Currency.RSD)
//
//    SpendingDetailsScreen(spending = spending, updateSpending = {}, onClickBack = {})
//}

@Preview
@Composable
private fun Temp() {
    AlertDialog(
        onDismissRequest = {
        },
        confirmButton = {
            Button(onClick = {
            }) {
                Text(text = "Change price")
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Text(
                    text = "Edit price",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        value = "324",
                        onValueChange = {},
                        label = { Text(text = "Price") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    CurrencyPickerDropdown(
                        selectedCurrencyState = Currency.EUR,
                        onCurrencySelected = { },
                        currencies = Currency.entries,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    )
}