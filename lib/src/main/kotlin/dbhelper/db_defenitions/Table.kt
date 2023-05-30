package dbhelper.db_defenitions

/**
 * A representation of a SQL TABLE
 *
 * @property name the name of the table
 * @constructor Creates an empty SQL TABLE with the [name] provided
 */
class Table(private val name: String) {
    private val cols: MutableList<Column> = mutableListOf()

    fun addPK(col: String = "id", auto_increment: Boolean = true) = apply {
        this.addColumn(Column(col, Type.INT).pk().autoIncrement(auto_increment))
    }

    private fun addColumn(vararg cols: Column) = apply {
        this.cols.addAll(cols)
    }

    fun getCreateSQLQuery(): String {
        val columns =
                cols.joinToString(separator = ",\n\t", prefix = "\n\t", postfix = "\n") {
                    it.getSQLQuery()
                }
        return "CREATE TABLE `$name`($columns);"
    }

    fun getDropSQLQuery(): String = "DROP TABLE `$name`;"
}
