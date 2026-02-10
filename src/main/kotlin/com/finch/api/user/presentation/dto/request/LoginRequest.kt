package com.finch.api.user.presentation.dto.request

data class LoginRequest(
    val email: String,
    val password: String
) {
}