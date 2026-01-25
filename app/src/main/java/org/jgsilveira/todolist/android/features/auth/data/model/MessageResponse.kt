package org.jgsilveira.todolist.android.features.auth.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MessageResponse(
    @SerialName("message") val message: String
)
