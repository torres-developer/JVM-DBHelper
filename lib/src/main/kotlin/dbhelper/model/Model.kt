package dbhelper.model

interface Model {
    fun populateFromMap(values: Map<String, Any>)
}
