package jvmdbhelper.model

import jvmdbhelper.DBProxy
import jvmdbhelper.db_defenitions.Table

abstract class TableManager<T : Model> {
    protected val table: Table by lazy {
        val table = Table(this.name())
        this.manageTable(table)
        table
    }

    abstract fun name(): String
    protected abstract fun manageTable(table: Table)

    fun createTable(): String = this.table.getCreateSQLQuery()
    fun deleteTable(): String = this.table.getDropSQLQuery()

    abstract fun init(): T

    fun create(dbh: DBProxy, values: Map<String, Any>): T {
        val keys = values.keys
        val cols = keys.joinToString(separator = ", ") { "`$it`" }
        val queries = keys.joinToString(separator = ", ") { "?" }
        val listValues = mutableListOf<Any>()
        for (v in keys) {
            listValues.add(values.get(v) ?: "NULL") // throw, default or null
        }

        dbh.exec("INSERT INTO `${this.name()}`($cols) VALUES ($queries);", listValues)

        val model = this.init()
        model.fromMap(values)
        return model
    }

    fun create(dbh: DBProxy, model: T): T {
        return this.create(dbh, model.getMutable())
    }

    fun update(dbh: DBProxy, model: T): T {
        val mutable = model.getMutable()
        val keys = mutable.keys
        val set = keys.joinToString(separator = ", ") { "$it=?" }
        val values = mutableListOf<Any>()
        for (v in keys) {
            values.add(mutable.get(v) ?: "NULL") // throw, default or null
        }
        val where = this.getModelFilter(model)
        values.addAll(where.values)

        dbh.exec("UPDATE `${this.name()}` SET $set ${where.statement};", values)

        return model;
    }

    fun delete(dbh: DBProxy, model: T): T {
        val where = this.getModelFilter(model)
        dbh.exec("DELETE FROM `${this.name()}` ${where.statement};", where.values)

        return model;
    }

    final protected fun getModelFilter(model: T): Where {
        val values = mutableListOf<Any>()
        val ids = model.getImmutable()
        val primaryKeys = this.table.getPrimaryKeys()
        val where = primaryKeys.joinToString(separator = " AND ") { "`$it`=?" }
        for (v in primaryKeys) {
            values.add(ids.get(v) ?: throw Exception())
        }

        return Where("WHERE $where", values)
    }
}

data class Where(val statement: String, val values: List<Any>) {}
