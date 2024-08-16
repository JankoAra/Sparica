package com.example.sparica.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.LocalDateTime


@Serializable
@Parcelize
@Entity(
    tableName = "spendings",
    foreignKeys = [ForeignKey(
        entity = Budget::class,
        parentColumns = ["id"],
        childColumns = ["budgetID"],
        onDelete = ForeignKey.CASCADE // Optionally, delete subcategories if the category is deleted
    )],
    indices = [Index(value = ["budgetID"])]
)
data class Spending(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var description: String,
    var price: Double,
    var category: SpendingCategory? = null,
    var subcategory: SpendingSubcategory? = null,
    val budgetID: Int? = null,
    var currency: Currency = Currency.RSD,
    @Contextual var date: LocalDateTime = LocalDateTime.now(),
    var deleted:Boolean = false,
    @Contextual var dateDeleted:LocalDate? = null

) : Parcelable {


    fun getFormatedPrice(): String {
        // Create a pattern based on the number of decimal places
        val pattern = if (currency.decimalPlaces > 0) {
            "#,##0.${"0".repeat(currency.decimalPlaces)}"
        } else {
            "#,##0"
        }
        val decimalFormat = DecimalFormat(pattern)

        // Format the price
        val formattedPrice = decimalFormat.format(price)
        return "$formattedPrice $currency"
    }

    override fun toString(): String {
        return "Spending(id=$id, description='$description', price=${getFormatedPrice()}, category=$category, subcategory=$subcategory, date=$date)"
    }

    fun asCsv(): String {
        return "$id,$description,$price,$currency,$category,$subcategory,${date.toLocalDate()},${date.toLocalTime()}"
    }

    companion object {
        fun csvHeader(): String {
            return "id,description,price,currency,category,subcategory,date,time"
        }
    }
}