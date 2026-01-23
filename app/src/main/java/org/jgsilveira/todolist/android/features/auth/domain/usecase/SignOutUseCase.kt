package org.jgsilveira.todolist.android.features.auth.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jgsilveira.todolist.android.features.auth.domain.repository.AuthRepository

class SignOutUseCase(
    private val authRepository: AuthRepository,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend operator fun invoke(): Result<Unit> {
        return runCatching {
            withContext(coroutineDispatcher) {
                authRepository.signOut()
            }
        }
    }
}