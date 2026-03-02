package org.jgsilveira.todolist.android.features.todo.domain.usecase

import java.util.UUID

class GenerateItemUuidUseCase {

    operator fun invoke(): String {
        return UUID.randomUUID().toString()
    }
}