package com.example.sparica.data.repositories.impl

import com.example.sparica.data.dao.SpendingCategoryDao
import com.example.sparica.data.models.SpendingCategory
import com.example.sparica.data.repositories.interfaces.SpendingCategoryRepository
import kotlinx.coroutines.flow.Flow

class SpendingCategoryRepositoryImpl(
    private val spendingCategoryDao: SpendingCategoryDao
) : SpendingCategoryRepository {
    override fun getAllEnabledCategories(): Flow<List<SpendingCategory>> =
        spendingCategoryDao.getAllEnabledCategories()

    override fun getCategoryByName(name: String): Flow<SpendingCategory> =
        spendingCategoryDao.getCategoryByName(name)

    override fun getCategoryById(id: Int): Flow<SpendingCategory> =
        spendingCategoryDao.getCategoryById(id)
}