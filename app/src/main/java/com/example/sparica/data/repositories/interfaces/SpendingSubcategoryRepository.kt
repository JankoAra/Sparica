package com.example.sparica.data.repositories.interfaces

import com.example.sparica.data.models.SpendingSubcategory
import kotlinx.coroutines.flow.Flow

interface SpendingSubcategoryRepository {
    fun getSubcategoriesForCategory(categoryId: Int): Flow<List<SpendingSubcategory>>

    fun getSubcategoryById(id: Int): Flow<SpendingSubcategory>

    fun getAllSubcategories(): Flow<List<SpendingSubcategory>>
}