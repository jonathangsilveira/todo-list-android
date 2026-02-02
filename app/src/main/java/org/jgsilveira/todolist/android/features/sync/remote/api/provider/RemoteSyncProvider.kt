package org.jgsilveira.todolist.android.features.sync.remote.api.provider

import org.jgsilveira.todolist.android.features.sync.remote.api.sync.RemoteSyncStrategy

object RemoteSyncProvider {
    private val remoteSyncStrategies = mutableMapOf<String, RemoteSyncStrategy>()

    fun add(
        name: String,
        remoteSyncStrategy: RemoteSyncStrategy
    ): Boolean {
        if (remoteSyncStrategies.containsKey(name)) {
            return false
        }
        remoteSyncStrategies[name] = remoteSyncStrategy
        return true
    }

    fun remove(name: String): Boolean {
        return remoteSyncStrategies.remove(name) != null
    }

    fun get(name: String): RemoteSyncStrategy? {
        return remoteSyncStrategies[name]
    }
}