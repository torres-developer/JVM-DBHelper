package jvmdbhelper.model

import jvmdbhelper.DBHost
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

    fun create(dbh: DBHost, values: Map<String, Any>): T {
        val keys = values.keys
        val cols = keys.joinToString(separator = ", ") { "`$it`" }
        val queries = keys.joinToString(separator = ", ") { "?" }
        val listValues = mutableListOf<Any>()
        for (v in keys) {
            listValues.add(v)
        }

        dbh.exec("INSERT INTO `$this.name`($cols) VALUES ($queries);", listValues)

        val model = this.init()
        model.populateFromMap(values)
        return model
    }
}
