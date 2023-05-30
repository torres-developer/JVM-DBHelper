package dbhelper

interface DBHost {
    fun exec(sql: String, values: List<Any>? = null)
}
