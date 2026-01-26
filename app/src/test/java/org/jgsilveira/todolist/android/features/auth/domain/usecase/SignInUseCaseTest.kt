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
internal class SignInUseCaseTest {
    private val testDispatcher = StandardTestDispatcher(
        scheduler = TestCoroutineScheduler()
    )

    private val authRepositoryMock = mockk<AuthRepository>()

    private val signInUseCase = SignInUseCase(
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
        val signInForm = SignInUseCaseStubs.signInForm
        coEvery { authRepositoryMock.signIn(signInForm) } just runs

        // When
        val result = signInUseCase(signInForm)

        // Then
        assert(result.isSuccess)
        assertEquals(result.getOrNull(), Unit)
        coVerify {
            authRepositoryMock.signIn(signInForm)
        }
    }

    @Test
    fun `invoke Should return Failure When repository fails`() = runTest {
        // Given
        val signInForm = SignInUseCaseStubs.signInForm
        coEvery {
            authRepositoryMock.signIn(signInForm)
        } throws Exception("Something went wrong")

        // When
        val result = signInUseCase(signInForm)

        // Then
        assert(result.isFailure)
        assertEquals(result.exceptionOrNull()?.message, "Something went wrong")
        coVerify {
            authRepositoryMock.signIn(signInForm)
        }
    }
}