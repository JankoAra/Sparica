package com.example.sparica.data.repositories.interfaces

import com.example.sparica.data.models.Budget
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {

    suspend fun insertBudget(budget: Budget):Long

    suspend fun updateBudget(budget: Budget)

    suspend fun deleteBudget(budget: Budget)

    fun getBudgetById(id: Int): Flow<Budget>

    suspend fun getBudgetByIdSync(id: Int): Budget

    fun getAllBudgets(): Flow<List<Budget>>
}