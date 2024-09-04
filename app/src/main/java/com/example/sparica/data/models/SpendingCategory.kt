package com.example.sparica.data.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Entity("spending_category", indices = [Index(value = ["name"], unique = true)])
@Serializable
@Parcelize
data class SpendingCategory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    var order: Int? = null,
    var enabled: Boolean = true,
    val custom: Boolean = false
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SpendingCategory

        return name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun toString(): String {
        return "SpendingCategory(id=$id, name='$name', order=$order, enabled=$enabled, custom=$custom)"
    }
}