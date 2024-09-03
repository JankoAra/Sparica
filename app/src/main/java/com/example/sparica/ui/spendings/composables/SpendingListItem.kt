@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.sparica.ui.spendings.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sparica.data.models.Currency
import com.example.sparica.data.models.Spending
import com.example.sparica.viewmodels.BudgetViewModel
import com.example.sparica.viewmodels.SpendingViewModel
import java.net.URLDecoder
import java.net.URLEncoder

@Composable
fun SpendingListItem(
    spending: Spending,
    budgetViewModel: BudgetViewModel,
    targetCurrency: Currency,
    onTap: () -> Unit
) {
    var convertedPrice by rememberSaveable {
        mutableStateOf(
            0.0
        )
    }
    LaunchedEffect(targetCurrency) {
        convertedPrice = budgetViewModel.convert(spending, targetCurrency).price

    }
    SpendingListItemContent(
        spending = spending,
        toDetails = { onTap() },
        convertedPrice = convertedPrice,
        targetCurrency = targetCurrency
    )

}

@Composable
fun SpendingListItemContent(
    spending: Spending,
    toDetails: () -> Unit,
    convertedPrice: Double,
    targetCurrency: Currency
) {
    TextButton(
        onClick = { toDetails() },
        shape = RectangleShape,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .border(BorderStroke(2.dp, MaterialTheme.colorScheme.primary), shape = RectangleShape)
            .padding(4.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = URLDecoder.decode(spending.description, "UTF-8"),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp),
            )
            Text(
                text = spending.category.toString(),
                style = TextStyle(fontSize = 14.sp)
            )
            spending.subcategory?.let {
                Text(text = "($it)", style = TextStyle(fontSize = 12.sp))
            }
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = spending.getFormatedPrice(),
                style = TextStyle(fontSize = 16.sp),
            )
            Text(
                text = "(${
                    spending.copy(price = convertedPrice, currency = targetCurrency)
                        .getFormatedPrice()
                })",
                style = TextStyle(fontSize = 16.sp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSpendingListItemContent() {
    val spending: Spending = Spending(0, "hamburgeri", 3444.0, null)
    val spending2: Spending =
        Spending(0, "hamburgerisdahdsghsagdsgdsadsh523dsfasdahhsda", 35453244.0, null)
    SpendingListItemContent(spending, {}, 3.4, Currency.EUR)
}
