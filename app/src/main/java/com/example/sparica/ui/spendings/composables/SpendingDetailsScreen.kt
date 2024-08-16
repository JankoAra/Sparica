package com.example.sparica.ui.spendings.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sparica.data.models.Currency
import com.example.sparica.data.models.Spending
import com.example.sparica.ui.util.MyTopAppBar
import com.example.sparica.ui.util.NavigateBackIconButton
import java.time.format.DateTimeFormatter

@Composable
fun SpendingDetailsScreen(
    spending: Spending,
    onClickBack: () -> Unit,
    updateSpending: (Spending) -> Unit
) {
    Scaffold(topBar = {
        MyTopAppBar(
            navigationIcon = { NavigateBackIconButton(onClickBack) },
        )
    }) {
        var showDialogEditDescription by rememberSaveable {
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
                            Column(modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)){
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
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Price:")
                Text(text = "${spending.price} ${spending.currency}")
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Category:")
                Text(text = spending.category.toString())
            }
            if (spending.subcategory != null) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Subcategory:")
                    Text(text = spending.subcategory.toString())
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Date and time:")
                val formatter = DateTimeFormatter.ofPattern("dd:MM:yyyy HH:mm")
                Text(text = spending.date.format(formatter))
            }
            Button(onClick = { onClickBack() }) {
                Text(text = "Go back")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSpendingDetailsScreen() {
    val spending = Spending(0, "nacosi", 123.124, null, currency = Currency.RSD)

    SpendingDetailsScreen(spending = spending, updateSpending = {}, onClickBack = {})
}