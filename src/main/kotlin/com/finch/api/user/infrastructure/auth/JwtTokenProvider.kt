package com.finch.api.user.infrastructure.auth

import com.finch.api.user.application.port.out.TokenProvider
import com.finch.global.common.domain.request.CustomUserDetails
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}")
    private val secretKey: String,

    @Value("\${jwt.access-token-expiration}")
    private val accessTokenExpiration: Long,

    @Value("\${jwt.refresh-token-expiration}")
    private val RefreshTokenExpiration: Long

): TokenProvider {

    /** 우리 서버  SecretKey로 객체를 생성 */
    private fun getSigningKey(): SecretKey =
        Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8))

    /** AccessToken 생성 */
    override fun createAccessToken(userId: Long, userName: String, role: String): String {
        val claims = Jwts.claims()
            .subject(userId.toString())
            .add("userName", userName)
            .add("role", role)
            .build();
        return createToken(claims, accessTokenExpiration)
    }

    /** RefreshToken 생성 */
    override fun createRefreshToken(userId: Long, userName: String, role: String): String {
        val claims = Jwts.claims()
            .subject(userId.toString())
            .add("userName", userName)
            .add("role", role)
            .build();
        return createToken(claims, RefreshTokenExpiration)
    }

    /** 헤더에서 토큰 값 추출 */
    override fun extractBearerToken(request: HttpServletRequest): String? {
        return request.getHeader("Authorization")
            ?.takeIf { it.startsWith("Bearer ", ignoreCase = true) }
            ?.substring(7)
            ?.trim()
    }

    /** 토큰 유효성 검증 */
    override fun validateToken(token: String) {
        try {
            Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
        } catch (e: ExpiredJwtException) {
            throw e // 토큰 만료시 필터에서 처리하도록 그대로 던짐
        } catch (e: Exception) {
            throw JwtException("유효하지 않은 토큰입니다.")
        }
    }

    /** 토큰에서 인증 정보 조회 */
    override fun getAuthentication(token: String): UsernamePasswordAuthenticationToken {
        val claims = Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .payload

        val userId = claims.subject.toLong()
        val userName = claims["userName"] as String
        val role = claims["role"] as String

        val userDetails = CustomUserDetails(
            userId = userId,
            userName = userName,
            role = role
        )

        return UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.authorities // Collection<? extends GrantedAuthority>
        )
    }

    /** 토큰 생성을 위한 서명키 메서드 */
    private fun createToken(claims: Claims, expiration: Long): String {
        val now = Date()
        val expiry = Date(now.time + expiration)

        return Jwts.builder()
            .claims(claims)
            .issuedAt(now)
            .expiration(expiry)
            .signWith(getSigningKey(), Jwts.SIG.HS256)
            .compact()
    }

}