package com.example.sparica.ui.budgets.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sparica.data.models.Budget

@Composable
fun BudgetListItem(budget: Budget, onClick: () -> Unit) {
    TextButton(
        onClick = { onClick() },
        shape = RectangleShape,
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
            .border(2.dp, MaterialTheme.colorScheme.secondary, RectangleShape)
            .padding(2.dp),
        colors = ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        Row {
            Text(
                text = budget.name,
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                style = TextStyle(fontSize = 20.sp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${budget.dateCreated}",
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                style = TextStyle(fontSize = 14.sp)
            )
        }


    }
}