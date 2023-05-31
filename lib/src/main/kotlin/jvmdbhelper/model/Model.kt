package jvmdbhelper.model

typealias Values = Map<String, Any>

interface Model {
    fun fromMap(values: Values)

    fun getImmutable(): Values
    fun getMutable(): Values
}
