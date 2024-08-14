package com.example.sparica.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
@Entity(tableName = "budgets")
data class Budget(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    @Contextual val dateCreated:LocalDate = LocalDate.now(),
    var defaultCurrency: Currency = Currency.RSD
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Budget

        if (id != other.id) return false
        if (name != other.name) return false
        if (dateCreated != other.dateCreated) return false
        return defaultCurrency == other.defaultCurrency
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + dateCreated.hashCode()
        result = 31 * result + defaultCurrency.hashCode()
        return result
    }

    override fun toString(): String {
        return "Budget(id=$id, name='$name', dateCreated=$dateCreated, defaultCurrency=$defaultCurrency)"
    }
}