package org.jgsilveira.todolist.android.core.logging

interface AppLogger {
    fun info(message: String)
    fun warn(message: String, throwable: Throwable? = null)
    fun error(message: String, throwable: Throwable? = null)
    fun debug(message: String, throwable: Throwable? = null)
}