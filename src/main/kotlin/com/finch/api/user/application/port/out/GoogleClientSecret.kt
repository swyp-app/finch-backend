package com.finch.api.user.application.port.out

import com.finch.api.user.infrastructure.social.google.dto.GoogleTokenDto

interface GoogleClientSecret {

    /** 구글 서버로 부터 인증 토큰 받기 */
    fun getGoogleAccessToken(code: String): GoogleTokenDto
}