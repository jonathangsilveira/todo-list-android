package org.jgsilveira.todolist.android.core.logging

import android.util.Log

internal class LogCatLogger(
    private val scopeName: String
): AppLogger {
    override fun info(message: String) {
        Log.i(scopeName, message)
    }

    override fun warn(message: String, throwable: Throwable?) {
        Log.w(scopeName, message, throwable)
    }

    override fun error(message: String, throwable: Throwable?) {
        Log.e(scopeName, message, throwable)
    }

    override fun debug(message: String, throwable: Throwable?) {
        Log.d(scopeName, message, throwable)
    }
}