package com.example.sparica.ui.budgets.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sparica.data.models.Budget

@Composable
fun BudgetListItem(budget: Budget, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(BorderStroke(2.dp, MaterialTheme.colorScheme.secondary))
            .padding(16.dp)
            .clickable { onClick() }
    ) {
        Text(text = "${budget.name}, created: ${budget.dateCreated}")
    }
}