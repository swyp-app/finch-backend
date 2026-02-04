package com.finch.api.user.infrastructure.social.kakao

import com.finch.api.user.application.port.out.KakaoClientSecret
import com.finch.api.user.infrastructure.social.kakao.dto.KakaoTokenDto
import com.finch.api.user.infrastructure.social.kakao.dto.KakaoUserInfoDto
import com.finch.api.user.infrastructure.social.kakao.dto.KakaoUserResponse
import com.finch.global.exception.handleException.InvalidAuthorizationException
import com.finch.global.exception.handleException.KakaoInvalidTokenResponseException
import com.finch.global.exception.handleException.KakaoInvalidUserResponseException
import com.finch.global.exception.handleException.KakaoTokenIssueFailedException
import com.finch.global.exception.handleException.KakaoUserInfoRetrieveFailedException
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestClient

@Component
class KakaoClientSecretImpl(
    @Value("\${KAKAO_CLIENT_ID}")
    private val clientId: String,

    @Value("\${KAKAO_REDIRECT_URL}")
    private val redirectUrl: String,

    private val restClient: RestClient = RestClient.create(),

): KakaoClientSecret {

    override fun getKakaoAccessToken(code: String): KakaoTokenDto {
        val params = LinkedMultiValueMap<String, String>().apply {
            add("grant_type", "authorization_code")
            add("client_id", clientId)
            add("redirect_uri", redirectUrl)
            add("code", code)
        }

        val responseBody = restClient.post()
            .uri(KAKAO_TOKEN_URL)
            .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
            .body(params)
            .retrieve()
            .onStatus({ it.is4xxClientError }) { request, response ->
                log.error("[KakaoAuth] 토큰 발급 실패 (4xx) - 상태코드: ${response.statusCode}")
                throw InvalidAuthorizationException()
            }
            .onStatus({ it.is5xxServerError }) { request, response ->
                log.error("[KakaoAuth] 토큰 발급 실패 (5xx) - 카카오 서버 오류")
                throw KakaoTokenIssueFailedException()
            }
            .body(KakaoTokenDto::class.java)

        return responseBody?.takeIf { it.accessToken != null }
            ?: throw KakaoInvalidTokenResponseException()
    }

    override fun getKakaoUserInfo(accessToken: String): KakaoUserInfoDto {
        val response = restClient.get()
            .uri(KAKAO_USERINFO_URL)
            .header("Authorization", "Bearer $accessToken")
            .retrieve()
            .onStatus({ it.is4xxClientError }) { _, httpResponse ->
                log.error("[KakaoAuth] 유저 조회 실패 (4xx) - 상태코드: ${httpResponse.statusCode}")
                throw InvalidAuthorizationException()
            }
            .onStatus({ it.is5xxServerError }) { _, httpResponse ->
                log.error("[KakaoAuth] 유저 조회 실패 (5xx) - 카카오 서버 오류: ${httpResponse.statusCode}")
                throw KakaoUserInfoRetrieveFailedException()
            }
            .body(KakaoUserResponse::class.java)

        val kakaoAccount = response?.kakaoAccount ?: throw KakaoInvalidUserResponseException()
        val profile = kakaoAccount.profile ?: throw KakaoInvalidUserResponseException()

        return KakaoUserInfoDto.of(providerId = response.id, kakaoAccount = kakaoAccount, profile = profile)
    }

    companion object {
        private const val KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token"
        private const val KAKAO_USERINFO_URL = "https://kapi.kakao.com/v2/user/me"
    }

}

private val log = KotlinLogging.logger {}
