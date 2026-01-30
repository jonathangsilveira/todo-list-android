package org.jgsilveira.todolist.android.features.sync.pending.impl.component

import org.jgsilveira.todolist.android.core.database.room.dao.PendingSyncRequestDao
import org.jgsilveira.todolist.android.features.sync.pending.api.component.PendingSyncRequestsComponent
import org.jgsilveira.todolist.android.features.sync.pending.api.model.PendingSyncRequest
import org.jgsilveira.todolist.android.features.sync.pending.api.model.PendingSyncRequestCreation
import org.jgsilveira.todolist.android.features.sync.pending.impl.mapper.toDomain
import org.jgsilveira.todolist.android.features.sync.pending.impl.mapper.toEntity

internal class PendingSyncRequestsComponentImpl(
    private val dao: PendingSyncRequestDao
) : PendingSyncRequestsComponent {
    override suspend fun findById(id: Long): PendingSyncRequest? {
        val entity = dao.findById(id)
        return entity?.toDomain()
    }

    override suspend fun add(pendingSyncAction: PendingSyncRequestCreation): PendingSyncRequest {
        val entity = pendingSyncAction.toEntity()
        val generatedId = dao.add(entity)
        return entity.copy(id = generatedId).toDomain()
    }

    override suspend fun update(pendingSyncRequest: PendingSyncRequest) {
        val entity = pendingSyncRequest.toEntity()
        dao.update(entity)
    }

    override suspend fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}