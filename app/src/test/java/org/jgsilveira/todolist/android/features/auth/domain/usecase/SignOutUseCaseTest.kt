package org.jgsilveira.todolist.android.features.auth.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.jgsilveira.todolist.android.features.auth.domain.repository.AuthRepository
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class SignOutUseCaseTest {
    private val testDispatcher = StandardTestDispatcher(
        scheduler = TestCoroutineScheduler()
    )

    private val authRepositoryMock = mockk<AuthRepository>()

    private val signOutUseCase = SignOutUseCase(
        authRepository = authRepositoryMock
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke Should return Success When repository succeeds`() = runTest {
        // Given
        coEvery { authRepositoryMock.signOut() } just runs

        // When
        val result = signOutUseCase()

        // Then
        assert(result.isSuccess)
        assertEquals(result.getOrNull(), Unit)
        coVerify {
            authRepositoryMock.signOut()
        }
    }

    @Test
    fun `invoke Should return Failure When repository fails`() = runTest {
        // Given
        coEvery {
            authRepositoryMock.signOut()
        } throws Exception("Something went wrong")

        // When
        val result = signOutUseCase()

        // Then
        assert(result.isFailure)
        assertEquals(result.exceptionOrNull()?.message, "Something went wrong")
        coVerify {
            authRepositoryMock.signOut()
        }
    }
}