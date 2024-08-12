package com.example.sparica.viewmodels

import android.app.Application
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sparica.data.database.SparicaDatabase
import com.example.sparica.data.models.Budget
import com.example.sparica.data.models.Spending
import com.example.sparica.data.repositories.BudgetRepository
import com.example.sparica.data.repositories.BudgetRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch

class BudgetViewModel(application: Application) : AndroidViewModel(application) {
    private val budgetRepository: BudgetRepository

    val _allBudgets = MutableStateFlow<List<Budget>>(emptyList())
    val allBudgets: StateFlow<List<Budget>> = _allBudgets.asStateFlow()



    init {
        val db = SparicaDatabase.getDatabase(application, viewModelScope)

        budgetRepository = BudgetRepositoryImpl(db.budgetDao())
        
        viewModelScope.launch { 
            budgetRepository.getAllBudgets().collect{
                _allBudgets.value = it
            }
        }
    }

    fun insertBudget(budget: Budget){
        viewModelScope.launch {
            budgetRepository.insertBudget(budget)
        }
    }

    fun deleteBudget(budget: Budget){
        viewModelScope.launch {
            budgetRepository.deleteBudget(budget)
        }
    }

}