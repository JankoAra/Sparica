package com.example.sparica.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sparica.data.models.Spending
import com.example.sparica.data.query_objects.SpendingInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface SpendingDao {
    @Insert
    suspend fun insert(spending: Spending)

    @Delete
    suspend fun delete(spending: Spending)

    @Update
    suspend fun update(spending: Spending)

    @Query("Select * from spendings order by date desc")
    fun getAllSpendings(): Flow<List<Spending>>

    @Query("Select * from spendings where budgetID = :budgetID and deleted = 0 order by date desc")
    fun getAllSpendingsForBudget(budgetID: Int): Flow<List<Spending>>

    @Query("Select * from spendings where deleted = 1 order by dateDeleted desc")
    fun getAllDeletedSpendings(): Flow<List<Spending>>

    @Query("Select * from spendings where id=:id")
    fun getSpending(id: Int): Flow<Spending?>

    @Query(
        "SELECT \n" +
                "        s.*, \n" +
                "        cat.name AS categoryName, \n" +
                "        sub.name AS subcategoryName\n" +
                "    FROM \n" +
                "        spendings s\n" +
                "    LEFT JOIN \n" +
                "        spending_category cat ON s.categoryID = cat.id\n" +
                "    LEFT JOIN \n" +
                "        spending_subcategory sub ON s.subcategoryID = sub.id\n" +
                "    WHERE \n" +
                "        s.budgetID = :budgetID \n" +
                "        AND s.deleted = 0\n" +
                "    ORDER BY \n" +
                "        s.date DESC"
    )
    fun getSpendingInfoForBudget(budgetID: Int): Flow<List<SpendingInfo>>

    @Query(
        "SELECT \n" +
                "        s.*, \n" +
                "        cat.name AS categoryName, \n" +
                "        sub.name AS subcategoryName\n" +
                "    FROM \n" +
                "        spendings s\n" +
                "    LEFT JOIN \n" +
                "        spending_category cat ON s.categoryID = cat.id\n" +
                "    LEFT JOIN \n" +
                "        spending_subcategory sub ON s.subcategoryID = sub.id\n" +
                "    ORDER BY \n" +
                "        s.date DESC"
    )
    fun getSpendingInfoAll(): Flow<List<SpendingInfo>>

}