package org.jgsilveira.todolist.android.core.logging

object AppLoggerProvider {
    fun scope(scopeName: String): AppLogger {
        return LogCatLogger(scopeName)
    }
}