package com.finch.api.user.application.service.command

import com.finch.api.user.application.mapper.LoginResponseMapper
import com.finch.api.user.application.port.`in`.SocialLoginUseCase
import com.finch.api.user.application.port.out.AppleClientSecret
import com.finch.api.user.application.port.out.KakaoClientSecret
import com.finch.api.user.application.port.out.UserRepository
import com.finch.api.user.domain.entity.User
import com.finch.api.user.domain.service.SocialUserInfoDto
import com.finch.api.user.domain.service.UserDomainService
import com.finch.api.user.presentation.dto.request.KakaoAppAuthRequest
import com.finch.api.user.presentation.dto.response.LoginResponse
import com.finch.global.common.domain.enums.Provider
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class SocialLoginService(
    private val kakaoClientSecret: KakaoClientSecret,
    private val userRepository: UserRepository,
    private val userDomainService: UserDomainService,
    private val loginResponseMapper: LoginResponseMapper,
    private val appleClientSecret: AppleClientSecret
) : SocialLoginUseCase {

    @Transactional
    override fun kakaoWebSocialLogin(code: String): LoginResponse {
        val kakaoToken = kakaoClientSecret.getKakaoAccessToken(code)
        val kakaoUserInfo = kakaoClientSecret.getKakaoUserInfo(kakaoToken.accessToken)

        val user = registerOrLogin(kakaoUserInfo, kakaoToken.refreshToken, Provider.KAKAO)
        return loginResponseMapper.toLoginResponse(user)
    }

    @Transactional
    override fun kakaoAppSocialLogin(request: KakaoAppAuthRequest): LoginResponse {
        val kakaoUserInfo = kakaoClientSecret.getKakaoUserInfo(request.accessToken)

        val user = registerOrLogin(kakaoUserInfo, request.refreshToken, Provider.KAKAO)
        return loginResponseMapper.toLoginResponse(user)
    }

    @Transactional
    override fun appleAppSocialLogin(code: String): LoginResponse {
        val clientSecret = appleClientSecret.createAppleClientSecret()
        val appleAuthToken = appleClientSecret.getAppleAuthToken(code, clientSecret)
        val appleUserInfo = appleClientSecret.getAppleUserInfo(appleAuthToken.idToken)

        val user = registerOrLogin(appleUserInfo, appleAuthToken.refreshToken, Provider.APPLE)
        return loginResponseMapper.toLoginResponse(user)
    }

    @Transactional
    override fun appleWebSocialLogin(code: String) {
        val clientSecret = appleClientSecret.createAppleWebClientSecret()
    }

    private fun registerOrLogin(
        userInfo: SocialUserInfoDto,
        socialRefreshToken: String,
        provider: Provider
    ): User {
        val existingUser = userRepository.findByProviderId(userInfo.providerId)
        val user = userDomainService.decideRegisterOrUpdate(existingUser, userInfo, socialRefreshToken, provider)
        return userRepository.save(user)
    }

}
