package com.example.sparica.data.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.sparica.data.dao.ExchangeRateDao
import com.example.sparica.data.dao.SpendingCategoryDao
import com.example.sparica.util.Converters
import com.example.sparica.data.dao.SpendingDao
import com.example.sparica.data.dao.SpendingSubcategoryDao
import com.example.sparica.data.database.migrations.MIGRATION_4_5
import com.example.sparica.data.database.migrations.MIGRATION_5_6
import com.example.sparica.data.models.ExchangeRate
import com.example.sparica.data.models.Spending
import com.example.sparica.data.models.SpendingCategory
import com.example.sparica.data.models.SpendingSubcategory
import com.example.sparica.viewmodels.SpendingViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Spending::class, SpendingCategory::class, SpendingSubcategory::class, ExchangeRate::class],
    version = 6,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SparicaDatabase : RoomDatabase() {
    abstract fun spendingDao(): SpendingDao
    abstract fun spendingCategoryDao(): SpendingCategoryDao
    abstract fun spendingSubcategoryDao(): SpendingSubcategoryDao
    abstract fun exchangeRateDao(): ExchangeRateDao

    companion object SparicaDatabaseProvider {
        @Volatile
        private var Instance: SparicaDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): SparicaDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, SparicaDatabase::class.java, "sparica_database")
                    //.fallbackToDestructiveMigration()
                    //.addCallback(DatabaseCallback(scope))
                    //.addMigrations(MIGRATION_4_5, MIGRATION_5_6)
                    .build()
                    .also {
                        Instance = it
                        scope.launch(Dispatchers.IO) {
                            populateDatabase(it.spendingCategoryDao(), it.spendingSubcategoryDao())
                            reorderCategoriesAndSubcategories(
                                it.spendingCategoryDao(),
                                it.spendingSubcategoryDao()
                            )
                        }
                    }
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Log.d("DatabaseCallback", "Database created, starting data population.")
                Instance?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        try {
//                            populateDatabase(
//                                database.spendingCategoryDao(),
//                                database.spendingSubcategoryDao()
//                            )
                            Log.d("DatabaseCallback", "Database populated successfully.")
                        } catch (e: Exception) {
                            Log.e("DatabaseCallback", "Error populating database: ${e.message}")
                        }
                    }
                }
            }
        }

        // Define categories and their subcategories
        val categories = mapOf(
            "Food" to listOf(
                "Breakfast",
                "Lunch",
                "Dinner",
                "Market",
                "etc"
            ),
            "Shopping" to listOf(
                "Clothes",
                "Tech",
                "Books",
                "Media",
                "Cosmetics",
                "Medicine",
                "etc"
            ),
            "Souvenirs" to listOf(
                "Personal",
                "Family",
                "Friends",
                "etc"
            ),
            "Attractions" to listOf(
                "Museum",
                "Entertainment",
                "etc"
            ),
            "Transport" to listOf(
                "Prepaid",
                "Bus",
                "Subway",
                "Train",
                "Car",
                "Ferry",
                "etc"
            ),
            "Uncategorized" to emptyList()
        )

        suspend fun populateDatabase(
            spendingCategoryDao: SpendingCategoryDao,
            spendingSubcategoryDao: SpendingSubcategoryDao
        ) {
            // Insert categories and their subcategories
            categories.entries.forEachIndexed { categoryIndex, (categoryName, subcategories) ->
                val existingCategory = spendingCategoryDao.getCategoryByNameSync(categoryName)
                val categoryId = existingCategory?.id
                    ?: spendingCategoryDao.insert(
                        SpendingCategory(
                            name = categoryName,
                            order = categoryIndex // Set the order based on the index in the category list
                        )
                    ).toInt()

                subcategories.forEachIndexed { subcategoryIndex, subcategoryName ->
                    val existingSubcategory =
                        spendingSubcategoryDao.getSubcategoryByNameSync(subcategoryName, categoryId)
                    if (existingSubcategory == null) {
                        spendingSubcategoryDao.insert(
                            SpendingSubcategory(
                                name = subcategoryName,
                                categoryId = categoryId,
                                order = subcategoryIndex // Set the order based on the index in the subcategory list
                            )
                        )
                    }
                }
            }
        }

        suspend fun reorderCategoriesAndSubcategories(
            spendingCategoryDao: SpendingCategoryDao,
            spendingSubcategoryDao: SpendingSubcategoryDao
        ) {
            categories.entries.forEachIndexed { categoryIndex, (categoryName, subcategories) ->
                val existingCategory = spendingCategoryDao.getCategoryByNameSync(categoryName)
                existingCategory!!.order = categoryIndex
                spendingCategoryDao.update(existingCategory)
                val categoryId = existingCategory.id

                subcategories.forEachIndexed { subcategoryIndex, subcategoryName ->
                    val existingSubcategory =
                        spendingSubcategoryDao.getSubcategoryByNameSync(subcategoryName, categoryId)
                    existingSubcategory!!.order = subcategoryIndex
                    spendingSubcategoryDao.update(existingSubcategory)
                }
            }
        }
    }
}