package jvmdbhelper.db_defenitions

class Column(private val name: String, private val type: Type) {
    private var unique = false
    private var nullable = true
    private var autoIncrement = false

    fun getName(): String = this.name
    fun getType(): Type = this.type

    fun pk() = apply {
        this.unique()
        this.nullable(false)
    }

    fun unique(b: Boolean = true) = apply {
        this.unique = b
    }

    fun nullable(b: Boolean = true) = apply {
        this.nullable = b
    }

    fun autoIncrement(b: Boolean = true) = apply {
        this.autoIncrement = b
    }

    fun getSQL(): String {
        return "`${this.name}` ${this.type.name}" +
                (if (this.unique) " UNIQUE" else "") +
                "${if (this.nullable) "" else " NOT"} NULL" +
                (if (this.autoIncrement) " AUTO_INCREMENT" else "")
    }

    fun isPrimaryKey(): Boolean = this.unique && !this.nullable
}
