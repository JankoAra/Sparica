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
import com.example.sparica.data.models.Currency
import com.example.sparica.data.models.Spending
import com.example.sparica.data.query_objects.SpendingInfo
import com.example.sparica.data.query_objects.extractSpendingFromInfo
import com.example.sparica.viewmodels.BudgetViewModel
import java.net.URLDecoder

@Composable
fun SpendingListItem(
    info: SpendingInfo,
    budgetViewModel: BudgetViewModel,
    targetCurrency: Currency,
    onTap: () -> Unit
) {
    var convertedPrice by rememberSaveable {
        mutableStateOf(
            0.0
        )
    }
    val spending by rememberSaveable {
        mutableStateOf(extractSpendingFromInfo(info))
    }
    LaunchedEffect(targetCurrency) {
        convertedPrice = budgetViewModel.convert(spending, targetCurrency).price

    }
    SpendingListItemContent(
        info = info,
        toDetails = { onTap() },
        convertedPrice = convertedPrice,
        targetCurrency = targetCurrency
    )

}

@Composable
fun SpendingListItemContent(
    info: SpendingInfo,
    toDetails: () -> Unit,
    convertedPrice: Double,
    targetCurrency: Currency
) {
    val spending by rememberSaveable {
        mutableStateOf(extractSpendingFromInfo(info))
    }
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
                text = URLDecoder.decode(info.description, "UTF-8"),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp),
            )
            Text(
                text = info.categoryName ?: "null",
                style = TextStyle(fontSize = 14.sp)
            )
            info.subcategoryID?.let {
                Text(text = "(${info.subcategoryName})", style = TextStyle(fontSize = 12.sp))
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
