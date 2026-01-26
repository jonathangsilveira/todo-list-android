package org.jgsilveira.todolist.android.features.auth.data.repository

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
import org.jgsilveira.todolist.android.features.auth.data.client.AuthClient
import org.jgsilveira.todolist.android.features.auth.data.provider.AuthTokensStore
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class AuthRepositoryTest {
    private val testDispatcher = StandardTestDispatcher(
        scheduler = TestCoroutineScheduler()
    )

    private val authClientMock = mockk<AuthClient> {
        coEvery { signIn(any()) } returns AuthRepositoryStubs.authTokens
        coEvery { signUp(any()) } just runs
        coEvery { signOut() } just runs
    }

    private val authTokensStoreMock = mockk<AuthTokensStore> {
        coEvery { clearAuthTokens() } just runs
        coEvery { storeAuthTokens(any()) } just runs
        coEvery { getAuthTokens() } returns AuthRepositoryStubs.authTokens
    }

    private val repository = AuthRepositoryImpl(
        authClient = authClientMock,
        authTokensStore = authTokensStoreMock
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `signIn Should store auth tokens When client returns auth tokens`() = runTest {
        // Given
        val form = AuthRepositoryStubs.signInForm
        val expectedAuthTokens = AuthRepositoryStubs.authTokens

        // When
        repository.signIn(form)

        // Then
        coVerify {
            authClientMock.signIn(form)
            authTokensStoreMock.storeAuthTokens(expectedAuthTokens)
        }
    }

    @Test
    fun `signIn Should not store auth tokens When client fails`() = runTest {
        // Given
        val form = AuthRepositoryStubs.signInForm
        coEvery { authClientMock.signIn(form) } throws Exception("Something went wrong")

        // When
        val exception = assertFails { repository.signIn(form) }

        // Then
        assertEquals(exception.message, "Something went wrong")
        coVerify {
            authClientMock.signIn(form)
        }
    }

    @Test
    fun `signUp Should return successfully When client succeeds`() = runTest {
        // Given
        val form = AuthRepositoryStubs.signUpForm

        // When
        repository.signUp(form)

        // Then
        coVerify {
            authClientMock.signUp(form)
        }
    }

    @Test
    fun `signUp Should throws an Exception When client fails`() = runTest {
        // Given
        val form = AuthRepositoryStubs.signUpForm
        coEvery { authClientMock.signUp(form) } throws Exception("Something went wrong")

        // When
        val exception = assertFailsWith<Exception> { repository.signUp(form) }

        // Then
        assertEquals(exception.message, "Something went wrong")
        coVerify {
            authClientMock.signUp(form)
        }
    }

    @Test
    fun `signOut Should clear auth tokens When client succeeds`() = runTest {
        // Given

        // When
        repository.signOut()

        // Then
        coVerify {
            authClientMock.signOut()
            authTokensStoreMock.clearAuthTokens()
        }
    }

    @Test
    fun `signOut Should throws an Exception When client fails`() = runTest {
        // Given
        coEvery { authClientMock.signOut() } throws Exception("Something went wrong")

        // When
        val exception = assertFailsWith<Exception> { repository.signOut() }

        // Then
        assertEquals(exception.message, "Something went wrong")
        coVerify {
            authClientMock.signOut()
        }
    }
}