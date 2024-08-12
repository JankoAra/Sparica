package com.example.sparica.data.repositories

import com.example.sparica.data.dao.SpendingCategoryDao
import com.example.sparica.data.models.SpendingCategory
import kotlinx.coroutines.flow.Flow

class SpendingCategoryRepositoryImpl(private val spendingCategoryDao: SpendingCategoryDao) {
    fun getAllCategories(): Flow<List<SpendingCategory>> = spendingCategoryDao.getAllCategories()

    fun getCategoryByName(name: String): Flow<SpendingCategory> =
        spendingCategoryDao.getCategoryByName(name)

    fun getCategoryById(id: Int): Flow<SpendingCategory> = spendingCategoryDao.getCategoryById(id)
}