package com.example.sparica.navigation

import com.example.sparica.data.models.Spending
import kotlinx.serialization.Serializable


@Serializable
data class BudgetDashboardRoute(val budgetID:Int)

@Serializable
data class SecondPath(val p: String?, val i: Int = 0)
//Int argument ne sme biti nullable?

@Serializable
data class SpendingDetailsRoute(val spending: Spending)

@Serializable
object ExchangeRateTableRoute

@Serializable
object BudgetsMainScreenRoute