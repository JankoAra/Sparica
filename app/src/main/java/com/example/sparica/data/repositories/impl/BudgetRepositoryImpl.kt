package com.example.sparica.data.repositories.impl

import com.example.sparica.data.dao.BudgetDao
import com.example.sparica.data.models.Budget
import com.example.sparica.data.repositories.interfaces.BudgetRepository
import kotlinx.coroutines.flow.Flow

class BudgetRepositoryImpl(private val budgetDao: BudgetDao) : BudgetRepository {

    override suspend fun insertBudget(budget: Budget): Long {
        return budgetDao.insertBudget(budget)
    }

    override suspend fun updateBudget(budget: Budget) {
        budgetDao.updateBudget(budget)
    }

    override suspend fun deleteBudget(budget: Budget) {
        budgetDao.deleteBudget(budget)
    }

    override fun getBudgetById(id: Int): Flow<Budget> {
        return budgetDao.getBudgetById(id)
    }

    override fun getAllBudgets(): Flow<List<Budget>> {
        return budgetDao.getAllBudgets()
    }
}
