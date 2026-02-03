package com.finch.api.user.application.service.command

import com.finch.api.user.application.mapper.LoginResponseMapper
import com.finch.api.user.application.port.`in`.SocialLoginUseCase
import com.finch.api.user.application.port.out.AppleClientSecret
import com.finch.api.user.application.port.out.KakaoClientSecret
import com.finch.api.user.application.port.out.UserRepository
import com.finch.api.user.domain.entity.User
import com.finch.api.user.domain.service.UserDomainService
import com.finch.api.user.infrastructure.social.kakao.dto.KakaoUserInfoDto
import com.finch.api.user.presentation.dto.request.KakaoAppAuthRequest
import com.finch.api.user.presentation.dto.response.LoginResponse
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
): SocialLoginUseCase {

    @Transactional
    override fun kakaoWebSocialLogin(code: String): LoginResponse {
        val kakaoToken = kakaoClientSecret.getKakaoAccessToken(code)
        val kakaoUserInfo = kakaoClientSecret.getKakaoUserInfo(kakaoToken.accessToken)

        val user = registerOrLogin(kakaoUserInfo, kakaoToken.refreshToken)
        return loginResponseMapper.toLoginResponse(user)
    }

    @Transactional
    override fun kakaoAppSocialLogin(request: KakaoAppAuthRequest): LoginResponse {
        val kakaoUserInfo = kakaoClientSecret.getKakaoUserInfo(request.accessToken)

        val user = registerOrLogin(kakaoUserInfo, request.refreshToken)
        return loginResponseMapper.toLoginResponse(user)
    }

    override fun appleAppSocialLogin(code: String) {
        val clientSecret = appleClientSecret.createAppleClientSecret();
    }

    private fun registerOrLogin(kakaoUser: KakaoUserInfoDto, socialRefreshToken: String): User {
        val existingUser = userRepository.findByProviderId(kakaoUser.providerId)
        val user = userDomainService.decideRegisterOrUpdate(existingUser, kakaoUserInfo = kakaoUser, socialRefreshToken)
        return userRepository.save(user)
    }

}
