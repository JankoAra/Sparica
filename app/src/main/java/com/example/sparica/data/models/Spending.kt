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
    foreignKeys = [
        ForeignKey(
            entity = Budget::class,
            parentColumns = ["id"],
            childColumns = ["budgetID"],
            onDelete = ForeignKey.CASCADE // Optionally, delete subcategories if the category is deleted
        ),
        ForeignKey(
            entity = SpendingCategory::class,
            parentColumns = ["id"],
            childColumns = ["categoryID"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SpendingSubcategory::class,
            parentColumns = ["id"],
            childColumns = ["subcategoryID"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
    ],
    indices = [
        Index(value = ["budgetID"]),
        Index(value = ["categoryID"]),
        Index(value = ["subcategoryID"])
    ]
)
data class Spending(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var description: String,
    var price: Double,
    var categoryID: Int? = null,
    var subcategoryID: Int? = null,
    val budgetID: Int? = null,
    var currency: Currency = Currency.RSD,
    @Contextual var date: LocalDateTime = LocalDateTime.now(),
    var deleted: Boolean = false,
    @Contextual var dateDeleted: LocalDate? = null

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
        return "Spending(id=$id, description='$description', price=${getFormatedPrice()}, category=$categoryID, subcategory=$subcategoryID, date=$date)"
    }
}

fun createSpending(
    id: Int = 0,
    description: String,
    price: Double,
    category: Int?,
    subcategory: Int?,
    budgetID: Int?,
    currency: Currency = Currency.RSD,
    date: LocalDateTime = LocalDateTime.now(),
    deleted: Boolean = false,
    dateDeleted: LocalDate? = null
): Spending {
    val encodedDescription = description
    return Spending(
        id,
        encodedDescription,
        price,
         category,
        subcategory,
        budgetID,
        currency,
        date,
        deleted,
        dateDeleted
    )
}
