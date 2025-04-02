package com.stb.preferences

import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val uid: String,
    val email: String?,
    val displayName: String?,
    val isEmailVerified: Boolean
)
