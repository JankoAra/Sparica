package com.example.sparica.ui.spendings.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sparica.data.models.Currency
import com.example.sparica.data.models.Spending
import com.example.sparica.data.models.SpendingCategory
import com.example.sparica.data.models.SpendingSubcategory
import com.example.sparica.viewmodels.BudgetViewModel
import com.example.sparica.viewmodels.SpendingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertSpendingForm(
    spendingViewModel: SpendingViewModel,
    budgetViewModel: BudgetViewModel,
    budgetID: Int
) {
    val activeBudget by budgetViewModel.activeBudget.collectAsState()

    var description by rememberSaveable { mutableStateOf("") }
    var priceString by rememberSaveable { mutableStateOf("") }
    var selectedCategory by rememberSaveable { mutableStateOf<SpendingCategory?>(null) }
    var selectedSubcategory by rememberSaveable { mutableStateOf<SpendingSubcategory?>(null) }
    var selectedCurrency by rememberSaveable {
        mutableStateOf<Currency>(
            activeBudget?.defaultCurrency ?: Currency.RSD
        )
    }

    LaunchedEffect(activeBudget) {
        selectedCurrency = activeBudget?.defaultCurrency ?: Currency.RSD
    }
    val focusManager = LocalFocusManager.current

    val descriptionFocusRequester = remember { FocusRequester() }
    val priceFocusRequester = remember { FocusRequester() }


    // Collecting categories and subcategory map from the ViewModel
    val categories by spendingViewModel.allCategories.collectAsState(emptyList())
    val subcategoryMap by spendingViewModel.subcategoryMap.collectAsState(emptyMap())
    // Update the selectedCategory once categories are loaded
    LaunchedEffect(categories) {
        if (categories.isNotEmpty() && selectedCategory == null) {
            var uncategorizedIndex =
                categories.indexOf(SpendingCategory(name = "Uncategorized", order = 0))
            if (uncategorizedIndex == -1) {
                uncategorizedIndex = 0
            }
            selectedCategory = categories[uncategorizedIndex] // Set the default selected category
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(descriptionFocusRequester),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Sentences
            ),
            keyboardActions = KeyboardActions(
                onNext = { priceFocusRequester.requestFocus() }
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = priceString,
                onValueChange = { priceString = it },
                label = { Text("Price") },
                modifier = Modifier
                    .focusRequester(priceFocusRequester)
                    .padding(end = 4.dp)
                    .weight(1f),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                )
            )
            CurrencyPickerDropdown(
                onCurrencySelected = { selectedCurrency = it },
                currencies = Currency.entries,
                selectedCurrencyState = selectedCurrency,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))


        // Display categories and handle category selection
        Text(text = "Category", style = MaterialTheme.typography.headlineSmall)

        RadioButtonGrid(
            options = categories,
            selectedOption = selectedCategory,
            onOptionSelected = {
                selectedCategory = it
                selectedSubcategory = null
            },
            labelProvider = { it.name }
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Display subcategories for the selected category
        selectedCategory?.let { category ->
            val subcategories = subcategoryMap[category] ?: emptyList()
            if (subcategories.isEmpty()) {
                selectedSubcategory = null
                return@let
            }
            if (selectedSubcategory == null) {
                var etcIndex = subcategories.indexOf(
                    SpendingSubcategory(
                        name = "etc",
                        categoryId = selectedCategory!!.id,
                        order = 0
                    )
                )
                if (etcIndex == -1) {
                    etcIndex = 0
                }
                selectedSubcategory = subcategories[etcIndex]
            }
            Text(text = "Subcategory", style = MaterialTheme.typography.headlineSmall)
            RadioButtonGrid(
                options = subcategories,
                selectedOption = selectedSubcategory,
                onOptionSelected = { selectedSubcategory = it },
                labelProvider = { it.name }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            focusManager.clearFocus()

            val spending = Spending(
                description = description,
                price = priceString.toDoubleOrNull() ?: 0.0,
                category = selectedCategory,
                subcategory = selectedSubcategory,
                currency = selectedCurrency,
                budgetID = budgetID
            )
            spendingViewModel.insert(spending)
        }) {
            Text(text = "Add spending")
        }
    }
}


//
//@Preview(showBackground = true)
//@Composable
//fun SpendingFormPreview() {
//    InsertSpendingForm(null)
//}

@Composable
fun <T> RadioButtonGrid(
    options: List<T>,
    selectedOption: T?,
    onOptionSelected: (T) -> Unit,
    labelProvider: (T) -> String,
    columns: Int = 2
) {
    NonLazyGrid(
        columns = columns, // Adjust the column count
        items = options,
        modifier = Modifier.fillMaxWidth()
    ) { option ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                .clickable { onOptionSelected(option) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = option == selectedOption,
                onClick = { onOptionSelected(option) },
                modifier = Modifier.padding(end = 4.dp)
            )
            // Adjust Text width
            Box(modifier = Modifier.weight(1f)) {
                ResizableText(text = labelProvider(option))
            }
        }

    }
}

@Composable
fun <T> NonLazyGrid(
    columns: Int,
    modifier: Modifier = Modifier,
    items: List<T>,
    content: @Composable (T) -> Unit
) {
    Column(modifier = modifier) {
        items.chunked(columns).forEach { rowItems ->
            Row(modifier = Modifier.fillMaxWidth()) {
                rowItems.forEach { item ->
                    Box(modifier = Modifier.weight(1f)) {
                        content(item)
                    }
                }

                // Fill remaining space with empty boxes if the last row isn't full
                if (rowItems.size < columns) {
                    for (i in 0 until (columns - rowItems.size)) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun ResizableText(
    text: String,
    maxFontSize: TextUnit = 16.sp,
    minFontSize: TextUnit = 4.sp
) {
    var textSize by remember { mutableStateOf(maxFontSize) }
    var isOverflowing by remember { mutableStateOf(false) }

    // Measure the text size initially and if it's overflowing
    BoxWithConstraints {
        val constraints = this.constraints
        //println("Available width: ${constraints.maxWidth}")

        Text(
            text = text,
            fontSize = textSize,
            onTextLayout = { textLayoutResult ->
                //println("Text: $text, Text width: ${textLayoutResult.size.width}, Overflowing: ${textLayoutResult.didOverflowWidth}")
                if (constraints.maxWidth <= textLayoutResult.size.width && textSize > minFontSize) {
                    //println("Overflow!!! Text: $text, Text width: ${textLayoutResult.size.width}, Overflowing: ${textLayoutResult.didOverflowWidth}")
                    isOverflowing = true
                } else {
                    isOverflowing = false
                }
            }
        )

        if (isOverflowing && textSize > minFontSize) {
            // Recursively reduce the font size until it fits within the constraints
            //println("Old textsize = ${textSize.value}")
            textSize = (textSize.value - 1).sp
            //println("New textsize = ${textSize.value}")

        }
    }
}
