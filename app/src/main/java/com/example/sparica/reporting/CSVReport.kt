package com.example.sparica.reporting

import com.example.sparica.data.database.SparicaDatabase
import com.example.sparica.data.models.Budget
import com.example.sparica.data.models.Currency
import com.example.sparica.data.models.Spending

fun spendingsToCSV(
    spendings: List<Spending>,
    budget: Budget,
    convert: (Spending, Currency) -> Spending
): String {
    val sep = "sep=,"
    //all spendings info
    val header =
        "description,price,currency,category,subcategory,datetime,${budget.defaultCurrency},RSD,EUR,USD"
    val data = mutableListOf<String>()
    for (s in spendings) {
        //description,price,currency,category,subcategory,datetime,budgetCurrency,RSD,EUR,USD
        val rsd = convert(s, Currency.RSD).price
        val eur = convert(s, Currency.EUR).price
        val usd = convert(s, Currency.USD).price
        val budgetDefault = convert(s, budget.defaultCurrency).price
        val line =
            "${s.description},${s.price},${s.currency},${s.category},${s.subcategory ?: "-"},${s.date},${budgetDefault},${rsd},${eur},${usd}"
        data.add(line)
    }
    //total amount spent
    val budgetCurrencyTotal = spendings.sumOf { convert(it, budget.defaultCurrency).price }
    val rsdTotal = spendings.sumOf { convert(it, Currency.RSD).price }
    val eurTotal = spendings.sumOf { convert(it, Currency.EUR).price }
    val usdTotal = spendings.sumOf { convert(it, Currency.USD).price }
    val totalLineHeader = ",,,,,TOTAL,${budget.defaultCurrency},RSD,EUR,USD"
    val totalLine = ",,,,,,$budgetCurrencyTotal,$rsdTotal,$eurTotal,$usdTotal"

    val categorySpending = mutableListOf<String>()
    categorySpending.add("Category,${budget.defaultCurrency},RSD,EUR,USD")
    val subcategorySpending = mutableListOf<String>()
    subcategorySpending.add("Subcategory,${budget.defaultCurrency},RSD,EUR,USD")

    //spent per category and subcategory
    SparicaDatabase.categories.entries.forEach { (category, subcategories) ->
        var categoryDefault = 0.0
        var categoryRsd = 0.0
        var categoryEur = 0.0
        var categoryUsd = 0.0
        subcategories.forEach { subcategory ->
            val spentInSubcategory = spendings
                .filter { it.subcategory?.name.equals(subcategory) }
                .sumOf { convert(it, budget.defaultCurrency).price }
            val temp = Spending(
                description = "",
                price = spentInSubcategory,
                currency = budget.defaultCurrency
            )
            val rsd = convert(temp, Currency.RSD).price
            val eur = convert(temp, Currency.EUR).price
            val usd = convert(temp, Currency.USD).price
            val subcatName = if (subcategory.equals("etc")) {
                "${category} (etc)"
            } else {
                subcategory
            }
            subcategorySpending.add("$subcatName,$spentInSubcategory,$rsd,$eur,$usd")
            categoryDefault += spentInSubcategory
            categoryRsd += rsd
            categoryEur += eur
            categoryUsd += usd
        }
        categorySpending.add("$category,$categoryDefault,$categoryRsd,$categoryEur,$categoryUsd")
    }

    return (listOf(sep, header) + data + listOf(
        totalLineHeader,
        totalLine
    ) + listOf("") + categorySpending + listOf("") + subcategorySpending).joinToString("\n")
}

