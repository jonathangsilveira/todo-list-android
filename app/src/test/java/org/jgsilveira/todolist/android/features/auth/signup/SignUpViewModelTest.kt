package org.jgsilveira.todolist.android.features.auth.signup

import app.cash.turbine.test
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.jgsilveira.todolist.android.coroutines.rules.MainDispatcherRule
import org.jgsilveira.todolist.android.features.auth.domain.usecase.SignUpUseCase
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

internal class SignUpViewModelTest {

    @get:Rule
    private val mainDispatcherRule = MainDispatcherRule()

    private val signUpUseCaseMock = mockk<SignUpUseCase>()

    @Test
    fun `init Should send initial state When view model is created`() = runTest {
        // Given
        val expectedState = SignUpViewState()

        // When
        val viewModel = createViewModel()

        // Then
        viewModel.viewState.test {
            assertEquals(expected = expectedState, actual = awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `updatePasswordVisibility Should change initial state When called`() = runTest {
        // Given
        val passwordVisibleState = SignUpViewState(isPasswordVisible = true)
        val viewModel = createViewModel()

        // When
        viewModel.updatePasswordVisibility(isVisible = true)

        // Then
        viewModel.viewState.test {
            assertEquals(passwordVisibleState, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `updateFullName Should change initial state When called`() = runTest {
        // Given
        val fullnameChangedState = SignUpViewState(fullName = "Chablau")
        val viewModel = createViewModel()

        // When
        viewModel.updateFullName(text = "Chablau")

        // Then
        viewModel.viewState.test {
            assertEquals(fullnameChangedState, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `updateEmailAddress Should change initial state When called`() = runTest {
        // Given
        val emailChangedState = SignUpViewState(fullName = "chablau@chablau.com")
        val viewModel = createViewModel()

        // When
        viewModel.updateEmailAddress(text = "chablau@chablau.com")

        // Then
        viewModel.viewState.test {
            assertEquals(emailChangedState, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `updatePassword Should change initial state When called`() = runTest {
        // Given
        val emailChangedState = SignUpViewState(fullName = "chablau")
        val viewModel = createViewModel()

        // When
        viewModel.updatePassword(text = "chablau")

        // Then
        viewModel.viewState.test {
            assertEquals(emailChangedState, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun createViewModel() = SignUpViewModel(
        signUpUseCase = signUpUseCaseMock
    )
}