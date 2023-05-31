package jvmdbhelper.db_defenitions

import jvmdbhelper.DBHelper

interface Migration {
    fun upgrade(dbh: DBHelper)
    fun downgrade(dbh: DBHelper)
}
