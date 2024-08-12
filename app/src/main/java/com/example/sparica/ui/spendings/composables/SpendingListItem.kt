package com.example.sparica.ui.spendings.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sparica.data.models.Currency
import com.example.sparica.data.models.Spending
import com.example.sparica.viewmodels.SpendingViewModel

@Composable
fun SpendingListItem(
    spending: Spending,
    spendingViewModel: SpendingViewModel = viewModel(),
    targetCurrency: Currency,
    onTap: () -> Unit
) {
    var convertedPrice by rememberSaveable {
        mutableStateOf(
            0.0
        )
    }
    LaunchedEffect(targetCurrency){
        convertedPrice = spendingViewModel.convertPrice(spending, targetCurrency).price
    }
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = {
                    println("Long press on id: ${spending.id}")
//                    coroutineScope.launch {
//                        repo?.deleteSpending(spending)
//                    }
                    spendingViewModel.delete(spending)
                }, onTap = {
                    onTap()
                })
            }
            .border(2.dp, MaterialTheme.colorScheme.secondary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = spending.description,
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp),
                )
                Text(
                    text = spending.category.toString() + (spending.subcategory?.toString()
                        ?.let { "($it)" } ?: ""),
                    style = TextStyle(fontSize = 14.sp)
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = spending.getFormatedPrice(),
                    style = TextStyle(fontSize = 16.sp),
                )
                Text(
                    text = "(${spending.copy(price = convertedPrice, currency = targetCurrency).getFormatedPrice()})",
                    style = TextStyle(fontSize = 16.sp),
                )
            }
        }
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(onClick = {
                spendingViewModel.delete(spending)
            }, modifier = Modifier.fillMaxWidth(0.5f)) {
                Text(text = "Delete")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSpendingListItem() {
    val spending: Spending = Spending(0, "hamburgeri", 35444.0, null)
    val spending2: Spending =
        Spending(0, "hamburgerisdahdsghsagdsgdsadsh523dsfasdahhsda", 35453244.0, null)
    SpendingListItem(spending = spending, targetCurrency = Currency.RSD) {}
}
