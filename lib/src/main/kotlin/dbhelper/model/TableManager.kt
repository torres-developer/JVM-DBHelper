package dbhelper.model

import dbhelper.DBHost
import dbhelper.db_defenitions.Table

abstract class TableManager<T : Model> {
    protected val table: Table by lazy {
        val table = Table(this.name())
        this.manageTable(table)
        table
    }

    abstract fun name(): String
    protected abstract fun manageTable(table: Table)

    // final public fun createTable(): String = this.table.createSQLQuery()
    // final public fun deleteTable(): String = this.table.dropSQLQuery()
    fun createTable(): String = ""
    fun deleteTable(): String = ""

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
