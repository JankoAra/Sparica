package com.example.sparica.reporting

import com.example.sparica.data.database.SparicaDatabase
import com.example.sparica.data.models.Budget
import com.example.sparica.data.models.Currency
import com.example.sparica.data.models.Spending
import com.example.sparica.data.query_objects.SpendingInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun spendingsToCSV(
    spendings: List<SpendingInfo>,
    budget: Budget,
    convert: (Spending, Currency) -> Spending
): String {
//    val sep = "sep=,"
//    val createdAt = "Created at: " + DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm")
//        .format(LocalDateTime.now())
//    //all spendings info
//    val header =
//        "description,price,currency,category,subcategory,datetime,${budget.defaultCurrency},RSD,EUR,USD"
//    val data = mutableListOf<String>()
//    for (s in spendings.reversed()) {
//        //description,price,currency,category,subcategory,datetime,budgetCurrency,RSD,EUR,USD
//        val rsd = String.format("%.2f", convert(s, Currency.RSD).price)
//        val eur = String.format("%.2f", convert(s, Currency.EUR).price)
//        val usd = String.format("%.2f", convert(s, Currency.USD).price)
//        val budgetDefault = String.format("%.2f", convert(s, budget.defaultCurrency).price)
//        val line =
//            "${s.description},${
//                String.format(
//                    "%.2f",
//                    s.price
//                )
//            },${s.currency},${s.categoryID},${s.subcategoryID ?: "-"},${s.date},${budgetDefault},${rsd},${eur},${usd}"
//        data.add(line)
//    }
//    //total amount spent
//    val budgetCurrencyTotal =
//        String.format("%.2f", spendings.sumOf { convert(it, budget.defaultCurrency).price })
//    val rsdTotal = String.format("%.2f", spendings.sumOf { convert(it, Currency.RSD).price })
//    val eurTotal = String.format("%.2f", spendings.sumOf { convert(it, Currency.EUR).price })
//    val usdTotal = String.format("%.2f", spendings.sumOf { convert(it, Currency.USD).price })
//    val totalLineHeader = ",,,,,TOTAL,${budget.defaultCurrency},RSD,EUR,USD"
//    val totalLine = ",,,,,,$budgetCurrencyTotal,$rsdTotal,$eurTotal,$usdTotal"
//
//    val categorySpending = mutableListOf<String>()
//    categorySpending.add("Category,${budget.defaultCurrency},RSD,EUR,USD")
//    val subcategorySpending = mutableListOf<String>()
//    subcategorySpending.add("Subcategory,${budget.defaultCurrency},RSD,EUR,USD")
//
//    //spent per category and subcategory
//    SparicaDatabase.categories.entries.forEach { (category, subcategories) ->
//        var categoryDefault = 0.0
//        var categoryRsd = 0.0
//        var categoryEur = 0.0
//        var categoryUsd = 0.0
//        subcategories.forEach { subcategory ->
//            val spentInSubcategory = spendings
//                .filter { it.subcategoryID?.name.equals(subcategory) && it.categoryID?.name.equals(category) }
//                .sumOf { convert(it, budget.defaultCurrency).price }
//            val temp = Spending(
//                description = "",
//                price = spentInSubcategory,
//                currency = budget.defaultCurrency
//            )
//            val rsd = String.format("%.2f", convert(temp, Currency.RSD).price)
//            val eur = String.format("%.2f", convert(temp, Currency.EUR).price)
//            val usd = String.format("%.2f", convert(temp, Currency.USD).price)
//            val subcatName = if (subcategory.equals("etc")) {
//                "${category} (etc)"
//            } else {
//                subcategory
//            }
//            subcategorySpending.add(
//                "$subcatName,${
//                    String.format(
//                        "%.2f",
//                        spentInSubcategory
//                    )
//                },$rsd,$eur,$usd"
//            )
//            categoryDefault += spentInSubcategory
//            categoryRsd += rsd.toDouble()
//            categoryEur += eur.toDouble()
//            categoryUsd += usd.toDouble()
//        }
//        if (subcategories.isEmpty()) {
//            //Uncategorized
//            categoryDefault = spendings
//                .filter { it.categoryID?.name.equals(category) }
//                .sumOf { convert(it, budget.defaultCurrency).price }
//            val temp = Spending(
//                price = categoryDefault,
//                currency = budget.defaultCurrency,
//                description = ""
//            )
//            categoryRsd = convert(temp, Currency.RSD).price
//            categoryEur = convert(temp, Currency.EUR).price
//            categoryUsd = convert(temp, Currency.USD).price
//        }
//        categorySpending.add(
//            "$category,${
//                String.format(
//                    "%.2f",
//                    categoryDefault
//                )
//            },${String.format("%.2f", categoryRsd)},${
//                String.format(
//                    "%.2f",
//                    categoryEur
//                )
//            },${String.format("%.2f", categoryUsd)}"
//        )
//
//    }
//
//    return (listOf(sep, createdAt, header) + data + listOf(
//        totalLineHeader,
//        totalLine
//    ) + listOf("") + categorySpending + listOf("") + subcategorySpending).joinToString("\n")
    return ""
}

