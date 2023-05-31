package jvmdbhelper.db_defenitions

/**
 * A representation of a SQL TABLE
 *
 * @property name the name of the table
 * @constructor Creates an empty SQL TABLE with the [name] provided
 */
class Table(private val name: String) {
    private val cols: MutableMap<String, Column> = mutableMapOf()

    fun addPK(col: String = "id", auto_increment: Boolean = true) = apply {
        this.addColumn(Column(col, Type.INT).pk().autoIncrement(auto_increment))
    }

    fun addColumn(vararg cols: Column) = apply {
        this.cols.putAll(cols.associateBy { it.getName() })
    }

    fun getCreateSQLQuery(): String {
        val columns =
            cols.values.joinToString(separator = ",\n\t", prefix = "\n\t", postfix = "\n") {
                it.getSQLQuery()
            }
        return "CREATE TABLE `$name`($columns);"
    }

    fun getDropSQLQuery(): String = "DROP TABLE `$name`;"

    fun getPrimaryKeys(): Set<String> {
        return this.cols.values.filter { it.isPrimaryKey() }.map { it.getName() }.toSet()
    }

    fun getColType(col: String): Type = this.cols[col]?.getType() ?: throw Exception()
}
