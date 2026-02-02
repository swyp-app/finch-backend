package com.finch.api.user.presentation.dto.response

import com.finch.api.user.domain.entity.User
import com.finch.global.common.domain.enums.Role

data class LoginResponse(
    val id: Long,
    val role: Role,
    val email: String?,
    val name: String,
    val profileImageUrl: String,
    val accessToken: String,
    val refreshToken: String
) {
    companion object {
        fun of(user: User, accessToken: String, refreshToken: String): LoginResponse {
            return LoginResponse(
                id = user.id,
                role = user.role,
                email = user.email,
                name = user.name,
                profileImageUrl = user.profileImageUrl ?: "",
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        }
    }
}