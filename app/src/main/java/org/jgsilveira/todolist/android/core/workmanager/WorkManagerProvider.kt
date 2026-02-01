package org.jgsilveira.todolist.android.core.workmanager

import android.app.Application
import androidx.work.WorkManager

interface WorkManagerProvider {
    fun get(): WorkManager
}

internal class WorkManagerProviderImpl(
    private val application: Application
): WorkManagerProvider {

    override fun get(): WorkManager {
        return WorkManager.getInstance(context = application)
    }

}