package org.jgsilveira.todolist.android.features.sync.remote.impl.job

import androidx.work.ListenableWorker
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.jgsilveira.todolist.android.core.database.room.dao.RemoteSyncRequestDao
import org.jgsilveira.todolist.android.coroutines.rules.MainDispatcherRule
import org.jgsilveira.todolist.android.features.sync.remote.api.provider.RemoteSyncProvider
import org.jgsilveira.todolist.android.features.sync.remote.api.sync.RemoteSyncStrategy
import org.jgsilveira.todolist.android.features.sync.remote.impl.job.WorkManagerSyncJobStubs.ENTITY_ID
import org.jgsilveira.todolist.android.features.sync.remote.impl.job.WorkManagerSyncJobStubs.STRATEGY_NAME
import org.jgsilveira.todolist.android.features.sync.remote.impl.job.WorkManagerSyncJobStubs.SYNC_REQUEST_ID
import org.jgsilveira.todolist.android.features.sync.remote.impl.job.WorkManagerSyncJobStubs.exception
import org.jgsilveira.todolist.android.features.sync.remote.impl.job.WorkManagerSyncJobStubs.syncRequestEntity
import org.junit.After
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

internal class WorkManagerSyncJobTest {
    @get:Rule
    private val mainDispatcherRule = MainDispatcherRule()

    private val daoMock = mockk<RemoteSyncRequestDao>()

    private val remoteSyncStrategyMock = mockk<RemoteSyncStrategy>()

    private val job = WorkManagerSyncJob(dao = daoMock)

    @After
    fun tearDown() {
        RemoteSyncProvider.remove(STRATEGY_NAME)
    }

    @Test
    fun `sync Should return success When no sync request has been found`() = runTest {
        // Given
        val input = syncJobParamsOf(syncRequestId = SYNC_REQUEST_ID)
        coEvery { daoMock.findById(SYNC_REQUEST_ID) } returns null

        // When
        val result = job.sync(input)

        // Then
        assertEquals(ListenableWorker.Result.success(), result)
    }

    @Test
    fun `sync Should return success When no strategy has been found`() = runTest {
        // Given
        val input = syncJobParamsOf(syncRequestId = SYNC_REQUEST_ID)
        coEvery { daoMock.findById(SYNC_REQUEST_ID) } returns syncRequestEntity

        // When
        val result = job.sync(input)

        // Then
        assertEquals(ListenableWorker.Result.success(), result)
    }

    @Test
    fun `sync Should return success When sync succeeds`() = runTest {
        // Given
        val input = syncJobParamsOf(syncRequestId = SYNC_REQUEST_ID)
        RemoteSyncProvider.add(STRATEGY_NAME, remoteSyncStrategyMock)
        coEvery { daoMock.findById(SYNC_REQUEST_ID) } returns syncRequestEntity
        coEvery { daoMock.deleteById(SYNC_REQUEST_ID) } just runs
        coEvery { remoteSyncStrategyMock.sync(ENTITY_ID, null) } just runs

        // When
        val result = job.sync(input)

        // Then
        assertEquals(ListenableWorker.Result.success(), result)
        coVerify {
            daoMock.findById(SYNC_REQUEST_ID)
            daoMock.deleteById(SYNC_REQUEST_ID)
            remoteSyncStrategyMock.sync(ENTITY_ID, null)
        }
    }

    @Test
    fun `sync Should return retry When sync fails and should retry`() = runTest {
        // Given
        val input = syncJobParamsOf(syncRequestId = SYNC_REQUEST_ID)
        RemoteSyncProvider.add(STRATEGY_NAME, remoteSyncStrategyMock)
        coEvery { daoMock.findById(SYNC_REQUEST_ID) } returns syncRequestEntity
        coEvery { daoMock.update(syncRequestEntity.copy(attempts = 1)) } just runs
        coEvery { remoteSyncStrategyMock.sync(ENTITY_ID, null) } throws exception
        coEvery { remoteSyncStrategyMock.shouldRetryOnError(1, exception) } returns true

        // When
        val result = job.sync(input)

        // Then
        assertEquals(ListenableWorker.Result.retry(), result)
        coVerify {
            daoMock.findById(SYNC_REQUEST_ID)
            daoMock.update(syncRequestEntity.copy(attempts = 1))
            remoteSyncStrategyMock.sync(ENTITY_ID, null)
            remoteSyncStrategyMock.shouldRetryOnError(1, exception)
        }
    }

    @Test
    fun `sync Should return success When sync fails and should not retry`() = runTest {
        // Given
        val input = syncJobParamsOf(syncRequestId = SYNC_REQUEST_ID)
        RemoteSyncProvider.add(STRATEGY_NAME, remoteSyncStrategyMock)
        coEvery { daoMock.findById(SYNC_REQUEST_ID) } returns syncRequestEntity
        coEvery { daoMock.deleteById(SYNC_REQUEST_ID) } just runs
        coEvery { remoteSyncStrategyMock.sync(ENTITY_ID, null) } throws exception
        coEvery { remoteSyncStrategyMock.shouldRetryOnError(1, exception) } returns false

        // When
        val result = job.sync(input)

        // Then
        assertEquals(ListenableWorker.Result.success(), result)
        coVerify {
            daoMock.findById(SYNC_REQUEST_ID)
            daoMock.deleteById(SYNC_REQUEST_ID)
            remoteSyncStrategyMock.sync(ENTITY_ID, null)
            remoteSyncStrategyMock.shouldRetryOnError(1, exception)
        }
    }
}