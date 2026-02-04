package com.finch.api.user.application.port.out

import com.finch.api.user.infrastructure.social.apple.dto.AppleTokenResponse
import com.finch.api.user.infrastructure.social.apple.dto.AppleUserInfoDto

interface AppleClientSecret {
    
    /** 애플 클라이언트 시크릿 키 생성 */
    fun createAppleClientSecret(isWeb: Boolean): String

    /** 애플 서버로 accessToken을 보내 인증 토큰 받기 */
    fun getAppleAuthToken(code: String, clientSecret: String, isWeb: Boolean): AppleTokenResponse

    /** idToken을 파싱해 사용자 정보 추출 */
    fun getAppleUserInfo(idToken: String): AppleUserInfoDto

}