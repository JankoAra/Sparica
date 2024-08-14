package com.example.sparica.data.repositories.impl

import com.example.sparica.data.dao.SpendingSubcategoryDao
import com.example.sparica.data.models.SpendingSubcategory
import com.example.sparica.data.repositories.interfaces.SpendingSubcategoryRepository
import kotlinx.coroutines.flow.Flow

class SpendingSubcategoryRepositoryImpl(
    private val spendingSubcategoryDao: SpendingSubcategoryDao
) : SpendingSubcategoryRepository {
    override fun getSubcategoriesForCategory(categoryId: Int): Flow<List<SpendingSubcategory>> =
        spendingSubcategoryDao.getSubcategoriesForCategory(categoryId)

    override fun getSubcategoryById(id: Int): Flow<SpendingSubcategory> =
        spendingSubcategoryDao.getSubcategoryById(id)

    override fun getAllSubcategories(): Flow<List<SpendingSubcategory>> =
        spendingSubcategoryDao.getAllSubcategories()
}