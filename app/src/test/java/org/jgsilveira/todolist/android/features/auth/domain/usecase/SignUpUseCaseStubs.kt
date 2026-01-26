package org.jgsilveira.todolist.android.features.auth.domain.usecase

import org.jgsilveira.todolist.android.features.auth.domain.model.SignUpForm

internal object SignUpUseCaseStubs {

    val signUpForm = SignUpForm(
        fullName = "Chablauzinho",
        email = "chablau@chablau.com",
        password = "chablau"
    )
}