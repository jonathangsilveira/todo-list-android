package org.jgsilveira.todolist.android.core.keystore

suspend inline fun <reified T> KeyValueStore.put(key: String, value: T?) {
    val decodedValue = when (T::class) {
        String::class -> value?.toString()
        Boolean::class -> value?.toString()
        Long::class -> value?.toString()
        Int::class -> value?.toString()
        Double::class -> value?.toString()
        Float::class -> value?.toString()
        else -> throw IllegalArgumentException("Illegal value for KeyValueStore: ${T::class}")
    }
    put(key, decodedValue)
}

suspend inline fun <reified T> KeyValueStore.get(key: String, defaultValue: T): T {
    val value = get(key)
    return (value as? T) ?: defaultValue
}