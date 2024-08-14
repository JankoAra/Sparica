package com.example.sparica.ui.budgets.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.example.sparica.data.models.Budget

@Composable
fun BudgetListItem(budget: Budget, onClick: () -> Unit) {
    TextButton(
        onClick = { onClick() },
        shape = RectangleShape,
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, MaterialTheme.colorScheme.secondary, RectangleShape)
            .padding(2.dp)
    ) {
        Text(
            text = "${budget.name}, created: ${budget.dateCreated}",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
        )
    }
}