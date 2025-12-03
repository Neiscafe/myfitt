package br.com.myfitt.common.data

import java.util.concurrent.ConcurrentHashMap

private const val CINCO_MINUTOS = 300000L

class MemoryCache<T>(val maxSize: Int, val timeout: Long = CINCO_MINUTOS, val id: (T) -> String) :
    MutableMap<String, T> {
    val items = ConcurrentHashMap<String, CacheItem<T>>()
    override val size: Int
        get() {
            removeExpirados()
            return items.size
        }

    private fun removeExpirados() {
        val currentMillis = System.currentTimeMillis()
        items.forEach { (id, it) ->
            if (it.validoOrNull(currentMillis) == null) {
                items.remove(id)
            }
        }
    }

    override fun isEmpty(): Boolean {
        removeExpirados()
        return items.isEmpty()
    }

    override fun containsKey(key: String): Boolean {
        removeExpirados()
        return items.containsKey(key)
    }

    override fun containsValue(value: T): Boolean {
        removeExpirados()
        return items.values.find { it.value == value } != null
    }

    override fun get(key: String): T? {
        removeExpirados()
        return items.get(key)?.value
    }

    class CacheItem<T>(
        val expireTime: Long,
        override val key: String,
        override val value: T
    ) : MutableMap.MutableEntry<String, T> {
        fun validoOrNull(currentMillis: Long = System.currentTimeMillis()): T? {
            return value.takeIf { expireTime < currentMillis }
        }

        override fun setValue(newValue: T): T {
            TODO("Not yet implemented")
        }
    }

    override val keys: MutableSet<String>
        get() {
            removeExpirados()
            return items.keys
        }
    override val values: MutableCollection<T>
        get() {
            removeExpirados()
            return items.values.map { it.value }.toMutableList()
        }
    override val entries: MutableSet<MutableMap.MutableEntry<String, T>>
        get() {
            removeExpirados()
            return items.entries.map { it.value }.toMutableSet()
        }

    override fun put(key: String, value: T): T? {
        return items.put(key, CacheItem(System.currentTimeMillis() + timeout, key, value))?.value
    }

    override fun remove(key: String): T? {
        return items.remove(key)?.value
    }

    override fun putAll(from: Map<out String, T>) {
        val currentMillis = System.currentTimeMillis()
        from.forEach { (id, it) ->
            items.put(id, CacheItem(currentMillis + timeout, id, it))
        }
    }

    override fun clear() {
        items.clear()
    }
}