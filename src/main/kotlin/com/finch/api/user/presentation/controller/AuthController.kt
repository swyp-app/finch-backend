package com.finch.api.user.presentation.controller

import com.finch.api.user.application.port.out.TokenProvider
import com.finch.global.common.domain.request.CustomUserDetails
import com.finch.global.common.domain.response.BaseResponse
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val tokenProvider: TokenProvider
) {

    @GetMapping("/test-token")
    fun getTestToken(
        @RequestParam userId: Long,
    ): BaseResponse<TokenResponse> {
        val userName = "user_$userId@finch.com"

        val accessToken = tokenProvider.createAccessToken(userId, userName, "USER")
        val refreshToken = tokenProvider.createRefreshToken(userId, userName, "USER")

        val data = TokenResponse(accessToken, refreshToken)
        return BaseResponse.ok(data)
    }

    @GetMapping("/me")
    fun test2(
        @AuthenticationPrincipal userDetails: CustomUserDetails
    ){
        log.info("userDetails = {}", userDetails.userId)
        log.info("userDetails = {}", userDetails.userName)
        log.info("userDetails = {}", userDetails.role)
    }
}

private val log = KotlinLogging.logger {}

// 응답용 DTO
data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String = "Bearer"
)