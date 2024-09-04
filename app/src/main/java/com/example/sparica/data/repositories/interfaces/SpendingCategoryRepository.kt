package com.example.sparica.data.repositories.interfaces

import com.example.sparica.data.models.SpendingCategory
import kotlinx.coroutines.flow.Flow

interface SpendingCategoryRepository {
    fun getAllEnabledCategories(): Flow<List<SpendingCategory>>

    fun getCategoryByName(name: String): Flow<SpendingCategory>

    fun getCategoryById(id: Int): Flow<SpendingCategory>
}