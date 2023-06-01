package jvmdbhelper.db_defenitions

import jvmdbhelper.DBHelper

typealias Migrations = Map<UInt, Migration>
typealias Seeders = Array<Seeder>

abstract class DB {
    abstract val name: String
    abstract val version: UInt

    protected abstract val migrations: Migrations
    protected abstract val seeders: Seeders

    fun migrate(db: DBHelper, from: UInt, to: UInt) {
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

    fun create(db: DBHelper) {
        this.migrate(db, 1u, this.version)

        for (s in this.seeders) {
            s.seed(db.proxy)
        }
    }
}
