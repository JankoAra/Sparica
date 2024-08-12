package com.example.sparica.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.sparica.data.models.SpendingSubcategory
import kotlinx.coroutines.flow.Flow


@Dao
interface SpendingSubcategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(subcategory: SpendingSubcategory): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(subcategories: List<SpendingSubcategory>)

    @Query("SELECT * FROM spending_subcategory WHERE categoryId = :categoryId order by `order`")
    fun getSubcategoriesForCategory(categoryId: Int): Flow<List<SpendingSubcategory>>

    @Query("SELECT * FROM spending_subcategory WHERE categoryId = :categoryId order by `order`")
    suspend fun getSubcategoriesForCategorySync(categoryId: Int): List<SpendingSubcategory>

    @Query("SELECT * FROM spending_subcategory WHERE id = :id")
    fun getSubcategoryById(id: Int): Flow<SpendingSubcategory>

    @Query("SELECT * FROM spending_subcategory")
    fun getAllSubcategories(): Flow<List<SpendingSubcategory>>

    @Query("SELECT * FROM spending_subcategory WHERE name = :name AND categoryId = :categoryId LIMIT 1")
    fun getSubcategoryByNameSync(name: String, categoryId: Int): SpendingSubcategory?

    @Update
    suspend fun update(spendingSubcategory: SpendingSubcategory)
}