package com.example.sparica.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sparica.data.api.ExchangeRateAPI
import com.example.sparica.data.database.SparicaDatabase
import com.example.sparica.data.models.Budget
import com.example.sparica.data.models.Currency
import com.example.sparica.data.models.ExchangeRate
import com.example.sparica.data.models.Spending
import com.example.sparica.data.repositories.interfaces.BudgetRepository
import com.example.sparica.data.repositories.impl.BudgetRepositoryImpl
import com.example.sparica.data.repositories.impl.ExchangeRateRepositoryImpl
import com.example.sparica.data.repositories.interfaces.ExchangeRateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class BudgetViewModel(application: Application) : AndroidViewModel(application) {
    private val budgetRepository: BudgetRepository
    private val exchangeRateRepository: ExchangeRateRepository

    private val _allBudgets = MutableStateFlow<List<Budget>>(emptyList())
    val allBudgets: StateFlow<List<Budget>> = _allBudgets.asStateFlow()

    private val _exchangeRates = MutableStateFlow<List<ExchangeRate>>(emptyList())
    val exchangeRates: StateFlow<List<ExchangeRate>> get() = _exchangeRates

    private val _activeBudgetId = MutableStateFlow<Int?>(null)

    val activeBudget = combine(_allBudgets, _activeBudgetId) { budgets, activeId ->
        if (activeId == null) {
            null
        } else {
            val newActive = budgets.firstOrNull { it.id == activeId }
            Log.d("BudgetViewModel", "new active budget set: $newActive")
            newActive
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)


    init {
        val db = SparicaDatabase.getDatabase(application, viewModelScope)
        exchangeRateRepository = ExchangeRateRepositoryImpl(db.exchangeRateDao(), ExchangeRateAPI)
        budgetRepository = BudgetRepositoryImpl(db.budgetDao())

        // get all budgets from db
        viewModelScope.launch {
            budgetRepository.getAllBudgets().collect {
                _allBudgets.value = it
                Log.d("BudgetViewModel", "_allBudgets assigned in init: ${_allBudgets.value}")
                Log.d("BudgetViewModel", "_allBudgets size in init: ${_allBudgets.value.size}")
            }
        }

        //get all latest exchange rates from db
        viewModelScope.launch {
            val rates = exchangeRateRepository.getLatestRates()
            //Log.d("SpendingViewModel", "Fetched exchange rates: $rates")
            _exchangeRates.value = rates
            Log.d("BudgetViewModel", "_exchangeRates assigned in init: ${_exchangeRates.value}")
            Log.d("BudgetViewModel", "_exchangeRates size in init: ${_exchangeRates.value.size}")
        }
    }

    fun insertBudget(budget: Budget) {
        viewModelScope.launch {
            budgetRepository.insertBudget(budget)
            Log.d("BudgetViewModel", "new Budget inserted: $budget")
            //Log.d("BudgetViewModel", "_allBudgets size after insert: ${_allBudgets.value.size}")
        }
    }

    fun deleteBudget(budget: Budget) {
        if (budget.id == _activeBudgetId.value) {
            Log.d("BudgetViewModel", "deleting active budget: $budget")
            setActiveBudgetById(null)
            Log.d("BudgetViewModel", "active budget set to null: ${activeBudget.value}")
        }
        viewModelScope.launch {
            budgetRepository.deleteBudget(budget)
        }
    }

    fun updateBudget(budget: Budget){
        viewModelScope.launch {
            budgetRepository.updateBudget(budget)
        }
    }

    fun setActiveBudgetById(id: Int?) {
        _activeBudgetId.update {
            id
        }
    }

    fun getLatestExchangeRates() {
        viewModelScope.launch {
            val rates = exchangeRateRepository.getLatestRates()
            Log.d("BudgetViewModel", "Fetched exchange rates: $rates")
            _exchangeRates.value = rates
        }
    }

    fun convert(spending: Spending, targetCurrency: Currency): Spending {
        val sourceCurrency = spending.currency
        val sourceRate =
            exchangeRates.value.first { it.targetCurrencyCode == sourceCurrency.name }.exchangeRate
        val targetRate =
            exchangeRates.value.first { it.targetCurrencyCode == targetCurrency.name }.exchangeRate
        return Spending(
            price = spending.price / sourceRate * targetRate,
            currency = targetCurrency,
            description = ""
        )
    }

//    fun convertPrice(spending: Spending, targetCurrency: Currency, onResult: (Spending) -> Unit) {
//        viewModelScope.launch {
//            val result = exchangeRateRepository.convert(
//                spending.currency.name,
//                targetCurrency.name,
//                spending
//            )
//            onResult(result)
//        }
//    }
//
//    suspend fun convertPriceSuspend(
//        spending: Spending,
//        selectedDisplayCurrency: Currency
//    ): Spending {
//        return suspendCoroutine { continuation ->
//            convertPrice(spending, selectedDisplayCurrency) { convertedSpending ->
//                continuation.resume(convertedSpending)
//            }
//        }
//    }

}