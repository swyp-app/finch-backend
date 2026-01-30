package com.finch.global.exception.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.finch.global.common.domain.response.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class LoginRequiredEntryPoint(
    private val objectMapper: ObjectMapper
) : AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        // 우리가 정의한 에러 응답 규격 생성
        val errorResponse = ErrorResponse.of(
            status = HttpStatus.UNAUTHORIZED,
            message = "로그인이 필요한 서비스입니다."
        )

        // HttpServletResponse에 직접 JSON 작성
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"

        response.writer.write(objectMapper.writeValueAsString(errorResponse))
    }
}