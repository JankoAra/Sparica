package com.example.sparica.ui.spendings.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sparica.data.models.Currency
import com.example.sparica.data.models.Spending
import java.time.format.DateTimeFormatter

@Composable
fun SpendingDetailsScreen(spending: Spending, onClickBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Description:")
            Text(text = spending.description)
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Price:")
            Text(text = "${spending.price} ${spending.currency?.toString() ?: ""}")
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

@Preview(showBackground = true)
@Composable
fun PreviewSpendingDetailsScreen() {
    val spending = Spending(0, "nacosi", 123.124, null, currency = Currency.RSD)

    SpendingDetailsScreen(spending = spending) {

    }
}