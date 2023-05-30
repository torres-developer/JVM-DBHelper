package jvmdbhelper.model

interface Model {
    fun populateFromMap(values: Map<String, Any>)
}
