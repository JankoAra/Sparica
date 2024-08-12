package com.example.sparica.viewmodels

import android.app.Application
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sparica.data.api.ExchangeRateAPI
import com.example.sparica.data.database.SparicaDatabase
import com.example.sparica.data.models.Currency
import com.example.sparica.data.models.ExchangeRate
import com.example.sparica.data.models.Spending
import com.example.sparica.data.models.SpendingCategory
import com.example.sparica.data.models.SpendingSubcategory
import com.example.sparica.data.repositories.ExchangeRateRepository
import com.example.sparica.data.repositories.ExchangeRateRepositoryImpl
import com.example.sparica.data.repositories.SpendingCategoryRepositoryImpl
import com.example.sparica.data.repositories.SpendingRepository
import com.example.sparica.data.repositories.SpendingRepositoryImpl
import com.example.sparica.data.repositories.SpendingSubcategoryRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class SpendingViewModel(application: Application) : AndroidViewModel(application) {
    private val spendingRepository: SpendingRepository
    private val spendingCategoryRepository: SpendingCategoryRepositoryImpl
    private val spendingSubcategoryRepository: SpendingSubcategoryRepositoryImpl
    private val exchangeRateRepository: ExchangeRateRepository


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

    private val _exchangeRates = MutableStateFlow<List<ExchangeRate>>(emptyList())
    val exchangeRates: StateFlow<List<ExchangeRate>> get() = _exchangeRates


    init {
        val db = SparicaDatabase.getDatabase(application, viewModelScope)
        val spendingDao = db.spendingDao()
        val categoryDao = db.spendingCategoryDao()
        val subcategoryDao = db.spendingSubcategoryDao()
        val exchangeRateDao = db.exchangeRateDao()
        spendingRepository = SpendingRepositoryImpl(spendingDao)
        spendingCategoryRepository = SpendingCategoryRepositoryImpl(categoryDao)
        spendingSubcategoryRepository = SpendingSubcategoryRepositoryImpl(subcategoryDao)
        exchangeRateRepository = ExchangeRateRepositoryImpl(exchangeRateDao, ExchangeRateAPI)

        viewModelScope.launch {
            // Fetch and update all spendings
            launch {
                spendingRepository.getAllSpendingsStream().collect {
                    Log.d("SpendingViewModel", "Fetched spendings: $it")
                    _allSpendings.value = it
                }
            }

            // Fetch categories and then fetch corresponding subcategories
            launch {
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

    fun getLatestExchangeRates() {
        viewModelScope.launch {
            val rates = exchangeRateRepository.getLatestRates()
            Log.d("SpendingViewModel", "Fetched exchange rates: $rates")
            _exchangeRates.value = rates
        }
    }

    suspend fun convertPrice(spending: Spending, targetCurrency: Currency): Spending {
//        val sourceRate = exchangeRates.firstOrNull()
//            ?.filter { exchangeRate -> exchangeRate.targetCurrencyCode == spending.currency.name }
//            ?.firstOrNull()?.exchangeRate ?: 1.0
//        val targetRate = exchangeRates.firstOrNull()
//            ?.filter { exchangeRate -> exchangeRate.targetCurrencyCode == targetCurrency.name }
//            ?.firstOrNull()?.exchangeRate ?: 1.0
//        return spending.copy(
//            price = spending.price / sourceRate * targetRate,
//            currency = targetCurrency
//        )


        return exchangeRateRepository.convert(spending.currency.name, targetCurrency.name, spending)
    }
}
