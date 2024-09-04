package com.example.sparica.data.repositories.impl

import com.example.sparica.data.dao.SpendingSubcategoryDao
import com.example.sparica.data.models.SpendingSubcategory
import com.example.sparica.data.repositories.interfaces.SpendingSubcategoryRepository
import kotlinx.coroutines.flow.Flow

class SpendingSubcategoryRepositoryImpl(
    private val spendingSubcategoryDao: SpendingSubcategoryDao
) : SpendingSubcategoryRepository {
    override fun getSubcategoriesForCategoryEnabled(categoryId: Int): Flow<List<SpendingSubcategory>> =
        spendingSubcategoryDao.getSubcategoriesForCategoryEnabled(categoryId)

    override fun getSubcategoryById(id: Int): Flow<SpendingSubcategory> =
        spendingSubcategoryDao.getSubcategoryById(id)

    override fun getAllSubcategories(): Flow<List<SpendingSubcategory>> =
        spendingSubcategoryDao.getAllSubcategories()
}