package jvmdbhelper.db_defenitions

import jvmdbhelper.DBProxy

interface Seeder {
    fun seed(proxy: DBProxy)
}