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

        db.execSQL(
            """
            CREATE TABLE `budgets` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `name` TEXT NOT NULL,
                `dateCreated` TEXT NOT NULL
            )
        """.trimIndent()
        )

        // Step 1: Create the new table with the updated schema
        db.execSQL(
            """
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
        """.trimIndent()
        )

        // Step 2: Copy the data from the old table to the new table
        db.execSQL(
            """
            INSERT INTO `spendings_new` (`id`, `description`, `price`, `category`, `subcategory`, `budgetID`, `currency`, `date`)
            SELECT `id`, `description`, `price`, `category`, `subcategory`, `budgetID`, `currency`, `date`
            FROM `spendings`
        """.trimIndent()
        )

        // Step 3: Drop the old table
        db.execSQL("DROP TABLE `spendings`")

        // Step 4: Rename the new table to the old table's name
        db.execSQL("ALTER TABLE `spendings_new` RENAME TO `spendings`")
    }
}

val MIGRATION_7_8 = object : Migration(7, 8) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Step 1: Create the index on the `budgetID` column in the `spendings` table
        db.execSQL("CREATE INDEX `index_spendings_budgetID` ON `spendings` (`budgetID`)")
    }

}

val MIGRATION_8_9 = object : Migration(8, 9) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("Alter table 'spendings' add column 'deleted' Integer not null default 0")
    }
}

val MIGRATION_9_10 = object : Migration(9, 10) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("Alter table 'spendings' add column 'dateDeleted' text")
    }
}

val MIGRATION_10_11 = object : Migration(10, 11) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("Alter table 'budgets' add column 'defaultCurrency' text not null default 'RSD'")
    }
}

val MIGRATION_11_12 = object : Migration(11, 12) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("delete from 'spending_category' where name = 'Miscellaneous'")
    }
}

val MIGRATION_12_13 = object : Migration(12, 13) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Step 1: Create the new table with the updated schema
        db.execSQL(
            """
            CREATE TABLE `spendings_new` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `description` TEXT NOT NULL,
                `price` REAL NOT NULL,
                `categoryID` INTEGER NULL,
                `subcategoryID` INTEGER NULL,
                `budgetID` INTEGER,
                `currency` TEXT NOT NULL,
                `date` INTEGER NOT NULL,
                `deleted` INTEGER NOT NULL DEFAULT 0,
                `dateDeleted` TEXT,
                FOREIGN KEY(`budgetID`) REFERENCES `budgets`(`id`) ON DELETE CASCADE,
                FOREIGN KEY(`categoryID`) REFERENCES `spending_category`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
                FOREIGN KEY(`subcategoryID`) REFERENCES `spending_subcategory`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
            )
        """.trimIndent()
        )

        // Step 2: Copy the data from the old table to the new table
        db.execSQL(
            """
            INSERT INTO `spendings_new` (`id`, `description`, `price`, `categoryID`, `subcategoryID`, `budgetID`, `currency`, `date`, `deleted`, `dateDeleted`)
            SELECT `id`, `description`, `price`, null, null, `budgetID`, `currency`, `date`, `deleted`, `dateDeleted`
            FROM `spendings`
        """.trimIndent()
        )

        // Step 3: Drop the old table
        db.execSQL("DROP TABLE `spendings`")

        // Step 4: Rename the new table to the old table's name
        db.execSQL("ALTER TABLE `spendings_new` RENAME TO `spendings`")
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_spendings_budgetID` ON `spendings` (`budgetID`)")
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_spendings_categoryID` ON `spendings` (`categoryID`)")
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_spendings_subcategoryID` ON `spendings` (`subcategoryID`)")
    }
}

val MIGRATION_13_14 = object : Migration(13, 14) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE `spendings_new` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `description` TEXT NOT NULL,
                `price` REAL NOT NULL,
                `categoryID` INTEGER NULL,
                `subcategoryID` INTEGER NULL,
                `budgetID` INTEGER,
                `currency` TEXT NOT NULL,
                `date` INTEGER NOT NULL,
                `deleted` INTEGER NOT NULL DEFAULT 0,
                `dateDeleted` TEXT,
                FOREIGN KEY(`budgetID`) REFERENCES `budgets`(`id`) ON DELETE CASCADE,
                FOREIGN KEY(`categoryID`) REFERENCES `spending_category`(`id`) ON DELETE SET NULL ON UPDATE CASCADE,
                FOREIGN KEY(`subcategoryID`) REFERENCES `spending_subcategory`(`id`) ON DELETE SET NULL ON UPDATE CASCADE
            )
        """.trimIndent()
        )

        // Step 2: Copy the data from the old table to the new table
        db.execSQL(
            """
            INSERT INTO `spendings_new` (`id`, `description`, `price`, `categoryID`, `subcategoryID`, `budgetID`, `currency`, `date`, `deleted`, `dateDeleted`)
            SELECT `id`, `description`, `price`, null, null, `budgetID`, `currency`, `date`, `deleted`, `dateDeleted`
            FROM `spendings`
        """.trimIndent()
        )

        // Step 3: Drop the old table
        db.execSQL("DROP TABLE `spendings`")

        // Step 4: Rename the new table to the old table's name
        db.execSQL("ALTER TABLE `spendings_new` RENAME TO `spendings`")
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_spendings_budgetID` ON `spendings` (`budgetID`)")
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_spendings_categoryID` ON `spendings` (`categoryID`)")
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_spendings_subcategoryID` ON `spendings` (`subcategoryID`)")

        db.execSQL("Alter table `spending_category` add column `enabled` Integer not null default 1")
        db.execSQL("Alter table `spending_category` add column `custom` Integer not null default 0")

        db.execSQL("Alter table `spending_subcategory` add column `enabled` Integer not null default 1")
        db.execSQL("Alter table `spending_subcategory` add column `custom` Integer not null default 0")
    }
}