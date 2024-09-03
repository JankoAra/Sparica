package com.example.sparica.data.repositories.interfaces

import com.example.sparica.data.models.Spending
import com.example.sparica.data.query_objects.SpendingInfo
import kotlinx.coroutines.flow.Flow

interface SpendingRepository {

    fun getAllSpendingsStream(): Flow<List<Spending>>


    fun getSpendingStream(id: Int): Flow<Spending?>

    fun getAllSpendingsForBudgetStream(budgetID: Int): Flow<List<Spending>>

    fun getAllDeletedSpendings(): Flow<List<Spending>>

    fun getSpendingInfoForBudget(budgetID: Int): Flow<List<SpendingInfo>>

    fun getSpendingInfoAll(): Flow<List<SpendingInfo>>


    suspend fun insertSpending(spending: Spending)


    suspend fun deleteSpending(spending: Spending)


    suspend fun updateSpending(spending: Spending)
}