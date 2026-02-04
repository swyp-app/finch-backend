package com.finch.api.user.infrastructure.social.apple

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.finch.api.user.application.port.out.AppleClientSecret
import com.finch.api.user.infrastructure.social.apple.dto.AppleTokenResponse
import com.finch.api.user.infrastructure.social.apple.dto.AppleUserInfoDto
import com.finch.api.user.infrastructure.social.apple.dto.AppleUserInfoDto.AppleTokenPayload
import com.finch.global.exception.handleException.*
import io.github.oshai.kotlinlogging.KotlinLogging
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestClient
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*

@Component
class AppleClientSecretImpl(

    @Value("\${APPLE_TEAM_ID}")
    private val teamId: String,

    @Value("\${APPLE_KEY_ID}")
    private val keyId: String,

    @Value("\${APPLE_KEY_ID}")
    private val clientServiceId: String,

    @Value("\${APPLE_CLIENT_ID}")
    private val clientId: String,

    @Value("\${APPLE_PRIVATE_KEY}")
    private val privateKeyP8: String,

    private val restClient: RestClient = RestClient.create(),
    private val objectMapper: ObjectMapper

): AppleClientSecret {

    companion object {
        private const val APPLE_TOKEN_URL = "https://appleid.apple.com/auth/token"
        private const val APPLE_AUDIENCE_URL = "https://appleid.apple.com"
    }

    override fun createAppleClientSecret(): String {
        return createAppleJwt(clientId)
    }

    override fun createAppleWebClientSecret(): String {
        return createAppleJwt(clientServiceId)
    }

    override fun getAppleAuthToken(code: String, clientSecret: String): AppleTokenResponse {
        val params: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>().apply {
            add("client_id", clientId)
            add("client_secret", clientSecret)
            add("code", code)
            add("grant_type", "authorization_code")
        }

        val responseBody = restClient.post()
            .uri(APPLE_TOKEN_URL)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(params)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError) { _, response ->
                log.error { "[AppleAuth] 토큰 발급 실패 (4xx) - 상태코드: ${response.statusCode}, 내용: ${String(response.body.readAllBytes())}" }
                throw InvalidAuthorizationException()
            }
            .onStatus(HttpStatusCode::is5xxServerError) { _, response ->
                log.error { "[AppleAuth] 토큰 발급 실패 (5xx) - 상태코드: ${response.statusCode}, 내용: ${String(response.body.readAllBytes())}" }
                throw AppleTokenIssueFailedException()
            }
            .body(AppleTokenResponse::class.java)

        return responseBody?.takeIf { it.accessToken != null }
            ?: throw AppleInvalidTokenResponseException()
    }

    override fun getAppleUserInfo(idToken: String): AppleUserInfoDto {
        val payload = parsePayload(idToken)

        return AppleUserInfoDto(
            providerId = payload.sub,
            email = payload.email
        )
    }

    private fun createAppleJwt(subjectId: String): String {
        val now = Date()
        val expiration = Date(now.time + 3600000)

        return Jwts.builder()

            .header()
            .keyId(keyId)                // kid
            .and()
            .issuer(teamId)              // iss
            .audience().add(APPLE_AUDIENCE_URL)// aud
            .and()
            .subject(subjectId)                // sub
            .issuedAt(now)               // iat
            .expiration(expiration)           // exp
            .signWith(getPrivateKey())  // 서명
            .compact()
    }

    private fun parsePayload(idToken: String): AppleTokenPayload {
        return runCatching {
            val chunks = idToken.split(".")
            if (chunks.size < 2) throw AppleTokenMalformedException()
            val decoded = String(Base64.getUrlDecoder().decode(chunks[1]))

            objectMapper.readValue<AppleTokenPayload>(decoded)
        }.getOrElse { throw InvalidAuthorizationException() }
    }

    private fun getPrivateKey(): PrivateKey {
        return try {
            val privateKeyContent = privateKeyP8
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("\\s+".toRegex(), "")

            // Base64 디코딩
            val encoded = Base64.getDecoder().decode(privateKeyContent)

            // KeyFactory를 이용해 PrivateKey 객체 생성 (Apple은 EC 알고리즘 사용)
            val keySpec = PKCS8EncodedKeySpec(encoded)
            val kf = KeyFactory.getInstance("EC")

            kf.generatePrivate(keySpec)
        } catch (e: Exception) {
            throw InvalidApplePrivateKeyException()
        }
    }

}

private val log = KotlinLogging.logger {}
