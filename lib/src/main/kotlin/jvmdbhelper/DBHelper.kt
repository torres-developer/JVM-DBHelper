package jvmdbhelper

import jvmdbhelper.model.Model
import jvmdbhelper.model.TableManager

class DBHelper(val proxy: DBProxy) {
    fun createTable(vararg models: TableManager<out Model>) {
        for (model in models) {
            this.proxy.exec(model.createTable())
        }
    }

    fun deleteTable(vararg models: TableManager<out Model>) {
        for (model in models) {
            this.proxy.exec(model.deleteTable())
        }
    }
}
