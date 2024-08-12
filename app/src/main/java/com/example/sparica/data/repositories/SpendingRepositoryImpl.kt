package com.example.sparica.data.repositories

import com.example.sparica.data.dao.SpendingDao
import com.example.sparica.data.models.Spending
import kotlinx.coroutines.flow.Flow

class SpendingRepositoryImpl(private val spendingDao: SpendingDao) : SpendingRepository {
    override fun getAllSpendingsStream(): Flow<List<Spending>> {
        return spendingDao.getAllSpendings()
    }

    override fun getSpendingStream(id: Int): Flow<Spending?> {
        return spendingDao.getSpending(id)
    }

    override fun getAllSpendingsForBudgetStream(budgetID: Int): Flow<List<Spending>> {
        return getAllSpendingsForBudgetStream(budgetID)
    }

    override suspend fun insertSpending(spending: Spending) {
        spendingDao.insert(spending)
    }

    override suspend fun deleteSpending(spending: Spending) {
        spendingDao.delete(spending)
    }

    override suspend fun updateSpending(spending: Spending) {
        spendingDao.update(spending)
    }
}