package dbhelper

import dbhelper.db_defenitions.Column
import dbhelper.db_defenitions.Table
import dbhelper.db_defenitions.Type
import dbhelper.model.Model
import dbhelper.model.TableManager

/*
TODO:
- DB_VER
- LOGS
- MIGRATIONS
- NOW
- CLOSE CONN
*/

class Item : Model {
    override fun populateFromMap(values: Map<String, Any>) {}

    companion object : TableManager<Item>() {
        const val TABLE_NAME = "title"
        const val COL_NAME_TITLE = "title"
        const val COL_NAME_URI = "uri"
        const val COL_NAME_STATE = "state"
        const val COL_NAME_CREATION_DATA = "creation_date"

        override fun name() = TABLE_NAME

        override fun manageTable(table: Table) {
            table.addPK()
                    .addColumn(
                            Column(COL_NAME_TITLE, Type.TXT),
                            Column(COL_NAME_URI, Type.TXT),
                            Column(COL_NAME_STATE, Type.INT),
                            Column(COL_NAME_CREATION_DATA, Type.DATETIME),
                    )
        }

        override fun init(): Item = Item()
    }
}

// typealias A = () -> Unit
// data class Struct()
