package com.example.sparica.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
@Entity(
    tableName = "spending_subcategory",
    foreignKeys = [ForeignKey(
        entity = SpendingCategory::class,
        parentColumns = ["id"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.CASCADE // Optionally, delete subcategories if the category is deleted
    )],
    indices = [Index(value = ["categoryId"])]
)
data class SpendingSubcategory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val categoryId: Int, // Foreign key referencing SpendingCategory
    var order: Int? = null,
    var enabled: Boolean = true,
    val custom: Boolean = false
) : Parcelable {
    override fun toString(): String {
        return name
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SpendingSubcategory

        if (name != other.name) return false
        return categoryId == other.categoryId
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + categoryId
        return result
    }
}