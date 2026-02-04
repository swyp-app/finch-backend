package com.finch.api.user.application.port.`in`

import com.finch.api.user.presentation.dto.request.KakaoAppAuthRequest
import com.finch.api.user.presentation.dto.response.LoginResponse

interface SocialLoginUseCase {
    
    /** 카카오 웹 소셜 로그인 */
    fun kakaoWebSocialLogin(code: String): LoginResponse
    
    /** 카카오 앱 소셜 로그인 */
    fun kakaoAppSocialLogin(request: KakaoAppAuthRequest): LoginResponse
    
    /** 애플 앱 소셜 로그인 */
    fun appleAppSocialLogin(code: String): LoginResponse
    
    /** 애플 웹 소셜 로그인 */
    fun appleWebSocialLogin(code: String): LoginResponse

}