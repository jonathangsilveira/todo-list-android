package org.jgsilveira.todolist.android.core.date

class GetCurrentTimeInMillisUseCase {

    operator fun invoke(): Long {
        return System.currentTimeMillis()
    }
}