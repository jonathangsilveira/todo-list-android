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
internal class SignUpUseCaseTest {
    private val testDispatcher = StandardTestDispatcher(
        scheduler = TestCoroutineScheduler()
    )

    private val authRepositoryMock = mockk<AuthRepository>()

    private val signUpUseCase = SignUpUseCase(
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
        val signUpForm = SignUpUseCaseStubs.signUpForm
        coEvery { authRepositoryMock.signUp(signUpForm) } just runs

        // When
        val result = signUpUseCase(signUpForm)

        // Then
        assert(result.isSuccess)
        assertEquals(result.getOrNull(), Unit)
        coVerify {
            authRepositoryMock.signUp(signUpForm)
        }
    }

    @Test
    fun `invoke Should return Failure When repository fails`() = runTest {
        // Given
        val signUpForm = SignUpUseCaseStubs.signUpForm
        coEvery {
            authRepositoryMock.signUp(signUpForm)
        } throws Exception("Something went wrong")

        // When
        val result = signUpUseCase(signUpForm)

        // Then
        assert(result.isFailure)
        assertEquals(result.exceptionOrNull()?.message, "Something went wrong")
        coVerify {
            authRepositoryMock.signUp(signUpForm)
        }
    }
}