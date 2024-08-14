package com.example.sparica.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BudgetViewModel(application: Application) : AndroidViewModel(application) {
    private val budgetRepository: BudgetRepository
    private val exchangeRateRepository: ExchangeRateRepository

    private val _allBudgets = MutableStateFlow<List<Budget>>(emptyList())
    val allBudgets: StateFlow<List<Budget>> = _allBudgets.asStateFlow()

    private val _activeBudget = MutableStateFlow<Budget?>(null)
    val activeBudget = _activeBudget.asStateFlow()

    private val _exchangeRates = MutableStateFlow<List<ExchangeRate>>(emptyList())
    val exchangeRates: StateFlow<List<ExchangeRate>> get() = _exchangeRates


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
        if(activeBudget.value?.equals(budget) ?:false){
            _activeBudget.value = null
        }
        viewModelScope.launch {
            budgetRepository.deleteBudget(budget)
        }
    }

    fun setActiveBudgetById(id: Int) {
        viewModelScope.launch {
            budgetRepository.getBudgetById(id).collect { b ->
                _activeBudget.update {
                    b
                }
            }
        }
    }

    fun getLatestExchangeRates() {
        viewModelScope.launch {
            val rates = exchangeRateRepository.getLatestRates()
            Log.d("BudgetViewModel", "Fetched exchange rates: $rates")
            _exchangeRates.value = rates
        }
    }

    fun convertPrice(spending: Spending, targetCurrency: Currency, onResult: (Spending) -> Unit) {
        // Launch a coroutine in a scope (this can be in a ViewModel or some other lifecycle-aware component)
        viewModelScope.launch {
            val result = exchangeRateRepository.convert(
                spending.currency.name,
                targetCurrency.name,
                spending
            )
            onResult(result)
        }
    }

}