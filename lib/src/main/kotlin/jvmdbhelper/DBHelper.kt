package jvmdbhelper

import jvmdbhelper.model.Model
import jvmdbhelper.model.TableManager

class DBHelper(private val dbh: DBHost) {
    fun createTable(vararg models: TableManager<out Model>) {
        for (model in models) {
            dbh.exec(model.createTable())
        }
    }
}
