package jvmdbhelper

import jvmdbhelper.model.Model
import jvmdbhelper.model.TableManager

abstract class DBHelper() {
    abstract protected var proxy: DBProxy

    final fun createTable(vararg models: TableManager<out Model>) {
        for (model in models) {
            this.proxy.exec(model.createTable())
        }
    }

    final fun deleteTable(vararg models: TableManager<out Model>) {
        for (model in models) {
            this.proxy.exec(model.deleteTable())
        }
    }
}
