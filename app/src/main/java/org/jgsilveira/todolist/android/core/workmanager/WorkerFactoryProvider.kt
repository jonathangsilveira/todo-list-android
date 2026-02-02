package org.jgsilveira.todolist.android.core.workmanager

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters

typealias WorkerFactoryBlock = (Context, WorkerParameters) -> ListenableWorker

object WorkerFactoryProvider {
    private val workerFactories = mutableMapOf<String, WorkerFactoryBlock>()

    fun registerWorkerFactory(
        className: String,
        block: WorkerFactoryBlock
    ) {
        workerFactories[className] = block
    }

    fun get(
        className: String,
        appContext: Context,
        params: WorkerParameters
    ): ListenableWorker? {
        return workerFactories[className]?.invoke(appContext, params)
    }
}
