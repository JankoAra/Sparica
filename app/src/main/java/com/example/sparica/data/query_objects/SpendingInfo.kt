package com.example.sparica.data.query_objects

import android.os.Parcelable
import com.example.sparica.data.models.Currency
import com.example.sparica.data.models.Spending
import com.example.sparica.data.models.createSpending
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

@Serializable
@Parcelize
data class SpendingInfo(
    val id: Int,
    var description: String,
    var price: Double,
    var categoryID: Int?,
    var subcategoryID: Int?,
    val budgetID: Int?,
    var currency: Currency,
    @Contextual var date: LocalDateTime,
    val deleted: Boolean,
    @Contextual val dateDeleted: LocalDate?,
    var categoryName: String?,
    var subcategoryName: String?


) : Parcelable {
    override fun toString(): String {
        return "SpendingInfo(id=$id, description='$description', price=$price, categoryID=$categoryID, subcategoryID=$subcategoryID, budgetID=$budgetID, currency=$currency, date=$date, deleted=$deleted, dateDeleted=$dateDeleted, categoryName='$categoryName', subcategoryName='$subcategoryName')"
    }
}

fun extractSpendingFromInfo(info:SpendingInfo):Spending{
    return createSpending(
        info.id,
        info.description,
        info.price,
        info.categoryID,
        info.subcategoryID,
        info.budgetID,
        info.currency,
        info.date,
        info.deleted,
        info.dateDeleted
    )
}