package com.example.sparica.data.repositories.interfaces

import com.example.sparica.data.models.Spending
import kotlinx.coroutines.flow.Flow

interface SpendingRepository {

    fun getAllSpendingsStream(): Flow<List<Spending>>


    fun getSpendingStream(id: Int): Flow<Spending?>

    fun getAllSpendingsForBudgetStream(budgetID: Int): Flow<List<Spending>>

    fun getAllDeletedSpendings(): Flow<List<Spending>>


    suspend fun insertSpending(spending: Spending)


    suspend fun deleteSpending(spending: Spending)


    suspend fun updateSpending(spending: Spending)
}