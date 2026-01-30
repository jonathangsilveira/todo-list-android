package org.jgsilveira.todolist.android.features.sync.pending.api.component

import org.jgsilveira.todolist.android.features.sync.pending.api.model.PendingSyncRequest
import org.jgsilveira.todolist.android.features.sync.pending.api.model.PendingSyncRequestCreation

interface PendingSyncRequestsComponent {
    suspend fun findById(id: Long): PendingSyncRequest?
    suspend fun add(pendingSyncAction: PendingSyncRequestCreation): PendingSyncRequest
    suspend fun update(pendingSyncRequest: PendingSyncRequest)
    suspend fun deleteById(id: Long)
}