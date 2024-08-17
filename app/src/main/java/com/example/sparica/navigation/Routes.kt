package com.example.sparica.navigation

import com.example.sparica.data.models.Spending
import kotlinx.serialization.Serializable


@Serializable
object BudgetsMainScreenRoute   //Shows list of created budgets and can create new budget

@Serializable
data class BudgetDashboardRoute(val budgetID: Int)   //Shows list of spendings for a specific budget. Spending insertion form.


@Serializable
data class SpendingDetailsRoute(val spending: Spending) //Details for a single spending

@Serializable
object ExchangeRateTableRoute   // Today's exchange rates table

@Serializable
object EditBudgetRoute  //Edit budget info screen(name, defaultCurrency)

@Serializable
object TrashCanRoute