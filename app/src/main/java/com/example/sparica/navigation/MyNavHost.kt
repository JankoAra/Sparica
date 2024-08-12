package com.example.sparica.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.sparica.data.models.Spending
import com.example.sparica.ui.budgets.composables.BudgetsMainScreen
import com.example.sparica.ui.composables.SecondPage
import com.example.sparica.ui.exchange.composables.ExchangeRateTable
import com.example.sparica.ui.spendings.composables.SingleBudgetScreen
import com.example.sparica.ui.spendings.composables.SpendingDetailsScreen
import com.example.sparica.util.CustomNavTypes
import com.example.sparica.viewmodels.BudgetViewModel
import com.example.sparica.viewmodels.SpendingViewModel
import kotlin.reflect.typeOf


@Composable
fun MyNavHost() {
    val navController = rememberNavController()
    val spendingViewModel: SpendingViewModel = viewModel<SpendingViewModel>()
    val budgetViewModel: BudgetViewModel = viewModel<BudgetViewModel>()
    NavHost(
        navController = navController,
        startDestination = BudgetsMainScreenRoute,
        enterTransition = { fadeIn(animationSpec = tween(0)) },
        exitTransition = { fadeOut(animationSpec = tween(0)) }
    ) {
        // Novi nacin rutiranja putem klasa kao ruta, beta verzija
        composable<BudgetDashboardRoute> {
            val args = it.toRoute<BudgetDashboardRoute>()
            SingleBudgetScreen(navController, spendingViewModel, args.budgetID)
        }
        composable<SecondPath> {
            val args = it.toRoute<SecondPath>()
            SecondPage(navController = navController, args.p, args.i)
        }
        composable<SpendingDetailsRoute>(
            typeMap = mapOf(typeOf<Spending>() to CustomNavTypes.SpendingType)
        ) {
            val args = it.toRoute<SpendingDetailsRoute>()
            SpendingDetailsScreen(spending = args.spending) { navController.popBackStack() }
        }
        composable<ExchangeRateTableRoute> {
            ExchangeRateTable(navController, spendingViewModel)
        }
        composable<BudgetsMainScreenRoute> {
            BudgetsMainScreen(navController, budgetViewModel)
        }
    }
}

// Stari nacin rutiranja putem stringova kao ruta
//@Composable
//fun MyNavHost() {
//    val navController = rememberNavController()
//    NavHost(navController = navController,
//        startDestination = "home",
//        enterTransition = { fadeIn(animationSpec = tween(0)) },
//        exitTransition = { fadeOut(animationSpec = tween(0)) }) {
//        composable("home") {
//            HomeScreen(navController)
//        }
//        composable(
//            "second/{param}", arguments = listOf(navArgument("param") { type = NavType.StringType })
//        ) {
//            val param = it.arguments?.getString("param")
//            SecondPage(navController = navController, param)
//        }
//    }
//}