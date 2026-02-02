package com.finch.api.user.application.mapper

import com.finch.api.user.application.port.out.TokenProvider
import com.finch.api.user.domain.entity.User
import com.finch.api.user.presentation.dto.response.LoginResponse
import org.springframework.stereotype.Component

@Component
class LoginResponseMapper(
    private val tokenProvider: TokenProvider
) {
    fun toLoginResponse(user: User): LoginResponse {
        val accessToken = tokenProvider.createAccessToken(
            userId = user.id,
            userName = user.name,
            role = user.role.name
        )

        val refreshToken = tokenProvider.createRefreshToken(
            userId = user.id,
            userName = user.name,
            role = user.role.name
        )

        return LoginResponse.of(user, accessToken,refreshToken)
    }

}