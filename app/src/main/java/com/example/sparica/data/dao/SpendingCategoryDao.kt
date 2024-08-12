package com.example.sparica.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.sparica.data.models.SpendingCategory
import kotlinx.coroutines.flow.Flow


@Dao
interface SpendingCategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<SpendingCategory>)

    @Insert
    suspend fun insert(category: SpendingCategory):Long

    @Query("Select * from spending_category order by `order`")
    fun getAllCategories(): Flow<List<SpendingCategory>>

    @Query("Select * from spending_category order by `order`")
    suspend fun getAllCategoriesSync(): List<SpendingCategory>

    @Query("Select * from spending_category where name=:name")
    fun getCategoryByName(name:String): Flow<SpendingCategory>

    @Query("Select * from spending_category where id=:id")
    fun getCategoryById(id:Int): Flow<SpendingCategory>

    @Query("SELECT * FROM spending_category WHERE name = :name LIMIT 1")
    suspend fun getCategoryByNameSync(name: String): SpendingCategory?

    @Update
    suspend fun update(spendingCategory: SpendingCategory)

}