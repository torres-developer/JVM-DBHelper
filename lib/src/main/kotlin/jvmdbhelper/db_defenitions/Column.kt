package jvmdbhelper.db_defenitions

open class Column(private val name: String, private val type: Type) {
    var unique = false
    var nullable = false
    var pk = false
        set(value) {
            if (value) {
                this.unique = true
                this.nullable = false
            }
            field = value
        }
    var autoIncrement = false

    fun getName(): String = this.name
    fun getType(): Type = this.type
    fun getSQL(): String = "`${this.name}` ${this.type.type}" + (if (this.pk) {
        " PRIMARY KEY"
    } else {
        ((if (this.unique) " UNIQUE" else "") + "${if (this.nullable) "" else " NOT"} NULL")
    }) + (if (this.autoIncrement) " AUTOINCREMENT" else "")
}
