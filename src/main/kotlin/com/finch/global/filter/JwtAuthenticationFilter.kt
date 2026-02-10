package com.finch.global.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.finch.api.user.application.port.out.TokenProvider
import com.finch.global.common.domain.response.ErrorResponse
import io.github.oshai.kotlinlogging.KotlinLogging
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

private val log = KotlinLogging.logger {}

class JwtAuthenticationFilter(
    val tokenProvider: TokenProvider
): OncePerRequestFilter() {

    private val excludePaths = listOf("/health", "/auth/")

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.requestURI
        // 요청 경로가 제외 목록에 포함되어 있으면 필터를 타지 않음 (true 반환)
        return excludePaths.any { path.startsWith(it) }
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        log.info { "================ doFilterInternal Action ================" }

        try {
            authenticateIfTokenExists(request)
            
            // 다음 필터로 요청 전달 (인증 성공/실패와 관계없이 진행)
            // - 인증 성공: SecurityContext에 인증 정보가 설정되어 있음
            // - 인증 실패/토큰 없음: SecurityContext가 비어있어 Spring Security가 자동으로 401 처리
            filterChain.doFilter(request, response)
        } catch (e: ExpiredJwtException) {
            // 토큰 만료 시 401 에러 응답 (리프레시 토큰으로 재발급 필요)
            setErrorResponse(response, HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다.")
        } catch (e: JwtException) {
            // JWT 파싱/검증 실패 (위변조, 잘못된 형식 등)
            setErrorResponse(response, HttpStatus.UNAUTHORIZED, e.message ?: "인증 오류가 발생했습니다.")
        } catch (e: Exception) {
            // 예상치 못한 에러 처리 (로그 남기고 500 에러 반환)
            log.error { "Unknown error in JwtAuthenticationFilter = ${e}" }
            setErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류 발생")
        }
    }

    private fun authenticateIfTokenExists(request: HttpServletRequest) {
        val token = tokenProvider.extractBearerToken(request) ?: return

        /*
        if (tokenBlacklistRepository.isBlacklisted(token)) {
            throw JwtException("로그아웃된 토큰입니다.")
        }
        */

        tokenProvider.validateToken(token)

        val auth = tokenProvider.getAuthentication(token)
        SecurityContextHolder.getContext().authentication = auth
    }

    private fun setErrorResponse(response: HttpServletResponse, status: HttpStatus, message: String) {
        response.status = status.value()
        response.contentType = "application/json;charset=UTF-8"

        val errorResponse = ErrorResponse.of(
            status = status,
            message = message
        )

        val objectMapper = ObjectMapper()
        response.writer.write(objectMapper.writeValueAsString(errorResponse))
    }

}