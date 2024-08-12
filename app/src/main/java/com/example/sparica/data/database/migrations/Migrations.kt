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

val MIGRATION_6_7 = object : Migration(6, 7) {
    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL("""
            CREATE TABLE `budgets` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `name` TEXT NOT NULL,
                `dateCreated` TEXT NOT NULL
            )
        """.trimIndent())

        // Step 1: Create the new table with the updated schema
        db.execSQL("""
            CREATE TABLE `spendings_new` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `description` TEXT NOT NULL,
                `price` REAL NOT NULL,
                `category` TEXT,
                `subcategory` TEXT,
                `budgetID` INTEGER,
                `currency` TEXT NOT NULL,
                `date` INTEGER NOT NULL,
                FOREIGN KEY(`budgetID`) REFERENCES `budgets`(`id`) ON DELETE CASCADE
            )
        """.trimIndent())

        // Step 2: Copy the data from the old table to the new table
        db.execSQL("""
            INSERT INTO `spendings_new` (`id`, `description`, `price`, `category`, `subcategory`, `budgetID`, `currency`, `date`)
            SELECT `id`, `description`, `price`, `category`, `subcategory`, `budgetID`, `currency`, `date`
            FROM `spendings`
        """.trimIndent())

        // Step 3: Drop the old table
        db.execSQL("DROP TABLE `spendings`")

        // Step 4: Rename the new table to the old table's name
        db.execSQL("ALTER TABLE `spendings_new` RENAME TO `spendings`")
    }
}

val MIGRATION_7_8 = object:Migration(7,8){
    override fun migrate(db: SupportSQLiteDatabase) {
        // Step 1: Create the index on the `budgetID` column in the `spendings` table
        db.execSQL("CREATE INDEX `index_spendings_budgetID` ON `spendings` (`budgetID`)")
    }

}