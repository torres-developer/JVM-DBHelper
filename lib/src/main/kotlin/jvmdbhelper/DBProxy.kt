package jvmdbhelper

import jvmdbhelper.model.Model
import jvmdbhelper.model.TableManager

interface DBProxy {
    fun exec(sql: String, values: List<Any> = listOf()): Long
    fun <T : Model> query(
        sql: String,
        values: List<Any> = listOf(),
        manager: TableManager<T>
    ): Iterable<T>

    fun close()
}
