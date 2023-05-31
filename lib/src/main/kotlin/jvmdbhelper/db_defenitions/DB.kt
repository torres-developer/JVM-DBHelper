package jvmdbhelper.db_defenitions

import jvmdbhelper.DBHelper

typealias Migrations = Map<UInt, Migration>

abstract class DB {
    abstract fun name(): String

    abstract fun migrations(): Migrations

    protected val migrations: Migrations by lazy { this.migrations() }

    final fun migrate(db: DBHelper, from: UInt, to: UInt) {
        val upgrading = from < to

        versions@ for (i in from..to) {
            val m = this.migrations.getOrElse(i) { continue@versions }
            if (upgrading) {
                m.upgrade(db)
            } else {
                m.downgrade(db)
            }
        }
    }

    final fun create(db: DBHelper) {
        this.migrations.get(1u)?.upgrade(db) ?: throw Exception("No migration for first version")
    }
}
