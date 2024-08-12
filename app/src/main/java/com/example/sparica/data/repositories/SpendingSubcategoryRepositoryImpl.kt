package com.example.sparica.data.repositories

import com.example.sparica.data.dao.SpendingSubcategoryDao
import com.example.sparica.data.models.SpendingSubcategory
import kotlinx.coroutines.flow.Flow

class SpendingSubcategoryRepositoryImpl(private val spendingSubcategoryDao: SpendingSubcategoryDao) {
    fun getSubcategoriesForCategory(categoryId: Int): Flow<List<SpendingSubcategory>> =
        spendingSubcategoryDao.getSubcategoriesForCategory(categoryId)


    fun getSubcategoryById(id: Int): Flow<SpendingSubcategory> =
        spendingSubcategoryDao.getSubcategoryById(id)

    fun getAllSubcategories(): Flow<List<SpendingSubcategory>> =
        spendingSubcategoryDao.getAllSubcategories()
}