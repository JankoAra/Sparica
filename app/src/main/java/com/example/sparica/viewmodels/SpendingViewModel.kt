package com.example.sparica.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sparica.data.database.SparicaDatabase
import com.example.sparica.data.models.Spending
import com.example.sparica.data.models.SpendingCategory
import com.example.sparica.data.models.SpendingSubcategory
import com.example.sparica.data.repositories.impl.SpendingCategoryRepositoryImpl
import com.example.sparica.data.repositories.interfaces.SpendingRepository
import com.example.sparica.data.repositories.impl.SpendingRepositoryImpl
import com.example.sparica.data.repositories.impl.SpendingSubcategoryRepositoryImpl
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class SpendingViewModel(application: Application) : AndroidViewModel(application) {
    private val spendingRepository: SpendingRepository
    private val spendingCategoryRepository: SpendingCategoryRepositoryImpl
    private val spendingSubcategoryRepository: SpendingSubcategoryRepositoryImpl

    //StateFlow cuva samo jednu vrednost (.value)
    //Obavestava svaki put kada promeni stanje
    private val _allSpendings = MutableStateFlow<List<Spending>>(emptyList())

    //allSpendings je javno polje sa get metodom koja vraca mutable _allSpendings StateFlow, ali ne moze da ga menja
    val allSpendings: StateFlow<List<Spending>> get() = _allSpendings

    private val _allCategories = MutableStateFlow<List<SpendingCategory>>(emptyList())
    val allCategories: StateFlow<List<SpendingCategory>> = _allCategories.asStateFlow()
    //ista funkcija kao get, radi eksplicitnu konverziju u immutable state flow

    private val _subcategoryMap =
        MutableStateFlow<Map<SpendingCategory, List<SpendingSubcategory>>>(emptyMap())
    val subcategoryMap: StateFlow<Map<SpendingCategory, List<SpendingSubcategory>>> get() = _subcategoryMap

    private val _deletedSpendings = MutableStateFlow<List<Spending>>(emptyList())
    val deletedSpendings = _deletedSpendings.asStateFlow()



    init {
        val db = SparicaDatabase.getDatabase(application, viewModelScope)
        val spendingDao = db.spendingDao()
        val categoryDao = db.spendingCategoryDao()
        val subcategoryDao = db.spendingSubcategoryDao()
        spendingRepository = SpendingRepositoryImpl(spendingDao)
        spendingCategoryRepository = SpendingCategoryRepositoryImpl(categoryDao)
        spendingSubcategoryRepository = SpendingSubcategoryRepositoryImpl(subcategoryDao)


        viewModelScope.launch {
            // Fetch categories and then fetch corresponding subcategories
            val categories =
                spendingCategoryRepository.getAllCategories().firstOrNull() ?: emptyList()
            Log.d("SpendingViewModel", "Fetched categories: $categories")
            _allCategories.value = categories

            val subcategoryMap = mutableMapOf<SpendingCategory, List<SpendingSubcategory>>()

            // Fetch subcategories for each category sequentially
            categories.forEach { category ->
                val subcategories =
                    spendingSubcategoryRepository.getSubcategoriesForCategory(category.id)
                        .firstOrNull() ?: emptyList()
                subcategoryMap[category] = subcategories
                Log.d(
                    "SpendingViewModel",
                    "Fetched subcategories for ${category.name}: $subcategories"
                )
            }

            Log.d("SpendingViewModel", "Final subcategoryMap: $subcategoryMap")
            _subcategoryMap.value = subcategoryMap
        }
        viewModelScope.launch {
            spendingRepository.getAllDeletedSpendings().collect{deleted ->
                _deletedSpendings.update {
                    deleted
                }
            }
        }


    }

    private var currentJobCollectSpendings: Job? = null

    fun getSpendingsForBudget(budgetID: Int) {
        currentJobCollectSpendings?.cancel()  // Cancel any previous collection
        currentJobCollectSpendings = viewModelScope.launch {
            spendingRepository.getAllSpendingsForBudgetStream(budgetID).collect { newSpendings ->
                _allSpendings.update {
                    newSpendings
                }
            }
        }
    }

    fun insert(spending: Spending) {
        viewModelScope.launch {
            spendingRepository.insertSpending(spending)
        }
    }

    fun delete(spending: Spending) {
        viewModelScope.launch {
            spendingRepository.deleteSpending(spending)
        }
    }

    fun updateSpending(spending: Spending){
        viewModelScope.launch {
            spendingRepository.updateSpending(spending)
        }
    }

    fun markDeleted(spending: Spending) {
        viewModelScope.launch {
            spending.deleted = true
            spending.dateDeleted = LocalDate.now()
            spendingRepository.updateSpending(spending)
        }
    }
}
