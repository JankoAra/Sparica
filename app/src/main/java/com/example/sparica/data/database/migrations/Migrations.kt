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

val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Step 1: Create temporary tables with the updated schema
        db.execSQL("""
            CREATE TABLE spending_subcategory_temp (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT NOT NULL,
                categoryId INTEGER NOT NULL,
                `order` INTEGER NULL DEFAULT 0 -- Changed to NULL
            )
        """)
        db.execSQL("""
            CREATE TABLE spending_category_temp (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT NOT NULL,
                `order` INTEGER NULL DEFAULT 0 -- Changed to NULL
            )
        """)

        // Step 2: Copy data from old tables to the new tables
        db.execSQL("""
            INSERT INTO spending_subcategory_temp (id, name, categoryId, `order`)
            SELECT id, name, categoryId, `order` FROM spending_subcategory
        """)
        db.execSQL("""
            INSERT INTO spending_category_temp (id, name, `order`)
            SELECT id, name, `order` FROM spending_category
        """)

        // Step 3: Drop the old tables
        db.execSQL("DROP TABLE spending_subcategory")
        db.execSQL("DROP TABLE spending_category")

        // Step 4: Rename the temporary tables to the original table names
        db.execSQL("ALTER TABLE spending_subcategory_temp RENAME TO spending_subcategory")
        db.execSQL("ALTER TABLE spending_category_temp RENAME TO spending_category")
    }
}
