@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.sparica.ui.spendings.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sparica.data.models.Currency

@Composable
fun CurrencyPickerDropdown(
    modifier: Modifier = Modifier,
    selectedCurrencyState: Currency,
    onCurrencySelected: (Currency) -> Unit,
    currencies: List<Currency>,
    label:String = "Currency"
) {
    var expanded by remember { mutableStateOf(false) }

    // Dropdown menu for currency selection
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
            .wrapContentSize()
    ) {
        OutlinedTextField(
            value = selectedCurrencyState.name,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
            },
            modifier = Modifier
                .menuAnchor(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            currencies.forEach { currency ->
                DropdownMenuItem(onClick = {
                    onCurrencySelected(currency)
                    expanded = false
                }, text = { Text(text = currency.name) })
            }
        }
    }
}