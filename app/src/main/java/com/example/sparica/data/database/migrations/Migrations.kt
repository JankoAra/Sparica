package com.example.sparica.data.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE spending_subcategory ADD COLUMN `order` INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE spending_category ADD COLUMN `order` INTEGER NOT NULL DEFAULT 0")

        // Optionally, you can set the order values here based on existing data
    }
}