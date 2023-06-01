package jvmdbhelper.model

import jvmdbhelper.DBProxy
import jvmdbhelper.db_defenitions.Table
import jvmdbhelper.db_defenitions.Type
import jvmdbhelper.db_defenitions.Where

abstract class TableManager<T : Model> {
    private val table: Table by lazy {
        val table = Table(this.name())
        this.manageTable(table)
        table
    }

    abstract fun name(): String
    protected abstract fun manageTable(table: Table)

    fun createTable(): String = this.table.getCreateSQLQuery()
    fun deleteTable(): String = this.table.getDropSQLQuery()

    abstract fun init(): T

    fun create(proxy: DBProxy, values: Map<String, Any>): T {
        val keys = values.keys
        val cols = keys.joinToString(separator = ", ") { "`$it`" }
        val queries = keys.joinToString(separator = ", ") { "?" }
        val listValues = mutableListOf<Any>()
        for (v in keys) {
            listValues.add(values[v] ?: "NULL") // throw, default or null
        }

        proxy.exec("INSERT INTO `${this.name()}`($cols) VALUES ($queries);", listValues)

        val model = this.init()
        model.fromMap(values)
        return model
    }

    fun create(proxy: DBProxy, model: T): T {
        return this.create(proxy, model.getMutable())
    }

    fun read(proxy: DBProxy, filter: Map<String, Any>): Iterable<T> {
        val keys = filter.keys
        val where = this.createWhereStatement(keys)
        val values = mutableListOf<Any>()
        for (v in keys) {
            values.add(filter[v] ?: throw Exception())
        }
        return if (where is String) {
            proxy.query("SELECT * FROM ${this.name()} $where;", values, this)
        } else {
            proxy.query("SELECT * FROM ${this.name()};", values, this)
        }
    }

    fun update(proxy: DBProxy, model: T): T {
        val mutable = model.getMutable()
        val keys = mutable.keys
        val set = keys.joinToString(separator = ", ") { "$it=?" }
        val values = mutableListOf<Any>()
        for (v in keys) {
            values.add(mutable[v] ?: "NULL") // throw, default or null
        }
        val where = this.getModelFilter(model)
        values.addAll(where.values)

        if (where.statement is String) {
            proxy.exec("UPDATE `${this.name()}` SET $set ${where.statement};", values)
        } else {
            proxy.exec("UPDATE `${this.name()}` SET $set;")
        }

        return model
    }

    fun delete(proxy: DBProxy, model: T): T {
        val where = this.getModelFilter(model)
        if (where.statement is String) {
            proxy.exec("DELETE FROM `${this.name()}` ${where.statement};", where.values)
        } else {
            proxy.exec("DELETE FROM `${this.name()}`;")
        }

        return model
    }

    private fun getModelFilter(model: T): Where {
        val values = mutableListOf<Any>()
        val ids = model.getImmutable()
        val primaryKeys = this.table.getPrimaryKeys()
        val where = this.createWhereStatement(primaryKeys)
        for (v in primaryKeys) {
            values.add(ids[v] ?: throw Exception())
        }

        return Where(where, values)
    }

    private fun createWhereStatement(keys: Set<String>): String? {
        return if (!keys.isEmpty()) {
            keys.joinToString(separator = " AND ", prefix = "WHERE ") { "`$it`=?" }
        } else {
            null
        }
    }

    fun getColType(col: String): Type = this.table.getColType(col)
}
