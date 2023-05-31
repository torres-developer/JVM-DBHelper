package jvmdbhelper

interface DBProxy {
    fun exec(sql: String, values: List<Any> = listOf())

    fun close()
}
