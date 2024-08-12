package com.example.sparica.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sparica.data.models.Spending
import kotlinx.coroutines.flow.Flow

@Dao
interface SpendingDao {
    @Insert
    suspend fun insert(spending: Spending)

    @Delete
    suspend fun delete(spending: Spending)

    @Update
    suspend fun update(spending: Spending)

    @Query("Select * from spendings order by id desc")
    fun getAllSpendings():Flow<List<Spending>>

    @Query("Select * from spendings where budgetID = :budgetID order by id desc")
    fun getAllSpendingsForBudget(budgetID:Int):Flow<List<Spending>>

    @Query("Select * from spendings where id=:id")
    fun getSpending(id:Int):Flow<Spending?>
}