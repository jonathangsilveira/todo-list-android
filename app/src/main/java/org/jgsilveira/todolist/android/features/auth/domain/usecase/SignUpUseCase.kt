package org.jgsilveira.todolist.android.features.auth.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jgsilveira.todolist.android.features.auth.domain.model.SignUpForm
import org.jgsilveira.todolist.android.features.auth.domain.repository.AuthRepository

class SignUpUseCase(
    private val authRepository: AuthRepository,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend operator fun invoke(form: SignUpForm): Result<Unit> {
        return runCatching {
            withContext(coroutineDispatcher) {
                authRepository.signUp(form)
            }
        }
    }
}