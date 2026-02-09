package com.finch.api.user.infrastructure.social.google

import com.finch.api.user.application.port.out.GoogleClientSecret
import com.finch.api.user.infrastructure.social.google.dto.GoogleTokenDto
import com.finch.global.exception.handleException.GoogleInvalidTokenResponseException
import com.finch.global.exception.handleException.GoogleTokenIssueFailedException
import com.finch.global.exception.handleException.InvalidAuthorizationException
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestClient

@Component
class GoogleClientSecretImpl(
    @Value("\${google.client-id}")
    private val clientId: String,

    @Value("\${google.client-secret}")
    private val clientSecret: String,

    @Value("\${google.redirect-uri}")
    private val redirectUrl: String,

    private val restClient: RestClient = RestClient.create(),
): GoogleClientSecret {

    companion object {
        private const val GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token"
    }

    override fun getGoogleAccessToken(code: String): GoogleTokenDto {
        val params = LinkedMultiValueMap<String, String>().apply {
            add("grant_type", "authorization_code")
            add("client_id", clientId)
            add("client_secret", clientSecret)
            add("redirect_uri", redirectUrl)
            add("code", code)
        }

        val responseBody = restClient.post()
            .uri(GOOGLE_TOKEN_URL)
            .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
            .body(params)
            .retrieve()
            .onStatus({ it.is4xxClientError }) { _, response ->
                log.error { "[GoogleAuth] 토큰 발급 실패 (4xx) - 상태코드: ${response.statusCode}" }
                throw InvalidAuthorizationException()
            }
            .onStatus({ it.is5xxServerError }) { _, response ->
                log.error { "[GoogleAuth] 토큰 발급 실패 (5xx) - 구글 서버 오류 : ${response.statusCode}" }
                throw GoogleTokenIssueFailedException()
            }
            .body(GoogleTokenDto::class.java)

        return responseBody?.takeIf { it.accessToken != null }
            ?: throw GoogleInvalidTokenResponseException()
    }

}

private val log = KotlinLogging.logger {}
