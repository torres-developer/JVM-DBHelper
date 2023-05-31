package jvmdbhelper.db_defenitions

import jvmdbhelper.DBHelper

typealias Migrations = Map<UInt, Migration>

abstract class DB {
    abstract fun name(): String
    abstract fun version(): UInt

    abstract fun genMigrations(): Migrations

    private val migrations: Migrations by lazy { this.genMigrations() }

    fun doMigration(db: DBHelper, from: UInt, to: UInt) {
        val upgrading = from < to

        for (i in from..to) {
            val m = this.migrations[i] ?: continue

            if (upgrading) {
                m.upgrade(db)
            } else {
                m.downgrade(db)
            }
        }
    }

    fun doCreation(db: DBHelper) {
        this.migrations[1u]?.upgrade(db) ?: throw Exception("No migration for first version")
    }
}
