package org.jgsilveira.todolist.android.features.sync.pending.impl.component

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import kotlinx.io.IOException
import org.jgsilveira.todolist.android.core.database.room.dao.PendingSyncRequestDao
import org.jgsilveira.todolist.android.coroutines.rules.MainDispatcherRule
import org.jgsilveira.todolist.android.features.sync.pending.impl.component.PendingSyncRequestsComponentStubs.GENERATED_ID
import org.jgsilveira.todolist.android.features.sync.pending.impl.component.PendingSyncRequestsComponentStubs.INVALID_ID
import org.jgsilveira.todolist.android.features.sync.pending.impl.component.PendingSyncRequestsComponentStubs.createPendingSyncRequest
import org.jgsilveira.todolist.android.features.sync.pending.impl.component.PendingSyncRequestsComponentStubs.createSyncRequest
import org.jgsilveira.todolist.android.features.sync.pending.impl.component.PendingSyncRequestsComponentStubs.databaseException
import org.jgsilveira.todolist.android.features.sync.pending.impl.component.PendingSyncRequestsComponentStubs.updatedPendingSyncRequest
import org.jgsilveira.todolist.android.features.sync.pending.impl.mapper.toEntity
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class PendingSyncRequestsComponentImplTest {
    @get:Rule
    private val mainDispatcherRule = MainDispatcherRule()

    private val daoMock = mockk<PendingSyncRequestDao> {
        coEvery { add(any()) } returns PendingSyncRequestsComponentStubs.GENERATED_ID
    }

    private val component = PendingSyncRequestsComponentImpl(daoMock)

    @Test
    fun `add Should return a pending sync request When dao succeeds`() = runTest {
        // Given
        val requestCreation = createSyncRequest
        val expectedSyncRequest = createPendingSyncRequest

        // When
        val actual = component.add(requestCreation)

        // Then
        assertEquals(expectedSyncRequest.id, actual.id)
        assertEquals(expectedSyncRequest.actionType, actual.actionType)
        assertEquals(expectedSyncRequest.entityId, actual.entityId)
        assertEquals(expectedSyncRequest.payload, actual.payload)
        coVerify {
            daoMock.add(any())
        }
    }

    @Test
    fun `add Should throws exception When dao fails`() = runTest {
        // Given
        val requestCreation = createSyncRequest
        coEvery { daoMock.add(any()) } throws databaseException

        // When
        val exception = assertFailsWith<IOException> { component.add(requestCreation) }

        // Then
        assertEquals("database error", exception.message)
        coEvery { daoMock.add(any()) }
    }

    @Test
    fun `update Should just runs When dao succeeds`() = runTest {
        // Given
        val pendingSyncRequest = updatedPendingSyncRequest
        val entity = pendingSyncRequest.toEntity()
        coEvery { daoMock.update(entity) } just runs

        // When
        component.update(pendingSyncRequest)

        // Then
        coVerify {
            daoMock.update(entity)
        }
    }

    @Test
    fun `update Should throw Exception When dao fails`() = runTest {
        // Given
        val pendingSyncRequest = updatedPendingSyncRequest
        val entity = pendingSyncRequest.toEntity()
        coEvery { daoMock.update(entity) } throws databaseException

        // When
        val exception = assertFailsWith<IOException> { component.update(pendingSyncRequest) }

        // Then
        assertEquals("database error", exception.message)
        coVerify {
            daoMock.update(entity)
        }
    }

    @Test
    fun `deleteById Should just runs When dao succeeds`() = runTest {
        // Given
        val id = GENERATED_ID
        coEvery { daoMock.deleteById(id) } just runs

        // When
        component.deleteById(id)

        // Then
        coVerify {
            daoMock.deleteById(id)
        }
    }

    @Test
    fun `deleteById Should throw Exception When dao fails`() = runTest {
        // Given
        val id = GENERATED_ID
        coEvery { daoMock.deleteById(id) } throws databaseException

        // When
        val exception = assertFailsWith<IOException> { component.deleteById(id) }

        // Then
        assertEquals("database error", exception.message)
        coVerify {
            daoMock.deleteById(id)
        }
    }

    @Test
    fun `findById Should return a pending sync request When dao succeeds`() = runTest {
        // Given
        val id = GENERATED_ID
        val expectedPendingSyncRequest = createPendingSyncRequest
        coEvery { daoMock.findById(id) } returns createPendingSyncRequest.toEntity()

        // When
        val actual = component.findById(id)

        // Then
        assertEquals(actual, expectedPendingSyncRequest)
        coVerify {
            daoMock.findById(id)
        }
    }

    @Test
    fun `findById Should return null When dao dont find any`() = runTest {
        // Given
        val id = INVALID_ID
        coEvery { daoMock.findById(id) } returns null

        // When
        val actual = component.findById(id)

        // Then
        assertEquals(actual, null)
        coVerify {
            daoMock.findById(id)
        }
    }

    @Test
    fun `findById Should throw Exception When dao fails`() = runTest {
        // Given
        val id = GENERATED_ID
        coEvery { daoMock.findById(id) } throws databaseException

        // When
        val exception = assertFailsWith<IOException> { component.findById(id) }

        // Then
        assertEquals("database error", exception.message)
        coVerify {
            daoMock.findById(id)
        }
    }
}