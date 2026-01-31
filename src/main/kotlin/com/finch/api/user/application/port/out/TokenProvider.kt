package com.finch.api.user.application.port.out

import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

interface TokenProvider {
    /** AccessToken 생성 */
    fun createAccessToken(userId: Long, userName: String, role: String): String

    /** RefreshToken 생성 */
    fun createRefreshToken(userId: Long, userName: String, role: String): String

    /** 헤더에서 토큰 값 추출 */
    fun extractBearerToken(request: HttpServletRequest): String?

    /** 토큰 유효성 검증 */
    fun validateToken(token: String)

    /** 토큰에서 인증 정보 조회 */
    fun getAuthentication(token: String): UsernamePasswordAuthenticationToken

}