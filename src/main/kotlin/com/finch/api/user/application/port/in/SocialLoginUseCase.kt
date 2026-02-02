package com.finch.api.user.application.port.`in`

import com.finch.api.user.presentation.dto.response.LoginResponse

interface SocialLoginUseCase {
    
    /** 카카오 웹 소셜 로그인 */
    fun kakaoWebSocialLogin(code: String): LoginResponse
}