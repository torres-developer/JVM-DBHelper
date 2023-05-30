package jvmdbhelper

interface DBHost {
    fun exec(sql: String, values: List<Any>? = null)
}
