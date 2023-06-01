package jvmdbhelper.db_defenitions

import jvmdbhelper.DBHelper

typealias Migrations = Map<UInt, Migration>
typealias Seeders = Map<UInt, Seeder>

abstract class DB {
    abstract fun name(): String
    abstract fun version(): UInt

    abstract fun migrations(): Migrations
    abstract fun seeders(): Seeders

    private val migrations: Migrations by lazy { this.migrations() }
    private val seeders: Seeders by lazy { this.seeders() }

    fun migrate(db: DBHelper, from: UInt, to: UInt) {
        val upgrading = from < to

        for (i in from..to) {
            val m = this.migrations[i] ?: continue

            if (upgrading) {
                m.upgrade(db)
                val s = this.seeders[i] ?: continue
                s.seed(db.proxy)
            } else {
                m.downgrade(db)
            }
        }
    }

    fun create(db: DBHelper) {
        this.migrations[1u]?.upgrade(db) ?: throw Exception("No migration for first version")
    }
}
