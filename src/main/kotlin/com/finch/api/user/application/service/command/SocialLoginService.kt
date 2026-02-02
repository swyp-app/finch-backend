package com.finch.api.user.application.service.command

import com.finch.api.user.application.mapper.LoginResponseMapper
import com.finch.api.user.application.port.`in`.SocialLoginUseCase
import com.finch.api.user.application.port.out.KakaoClientSecret
import com.finch.api.user.application.port.out.UserRepository
import com.finch.api.user.domain.entity.User
import com.finch.api.user.domain.service.UserDomainService
import com.finch.api.user.infrastructure.social.kakao.dto.KakaoUserInfoDto
import com.finch.api.user.presentation.dto.response.LoginResponse
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class SocialLoginService(
    private val kakaoClientSecret: KakaoClientSecret,
    private val userRepository: UserRepository,
    private val userDomainService: UserDomainService,
    private val loginResponseMapper: LoginResponseMapper
): SocialLoginUseCase {

    @Transactional
    override fun kakaoWebSocialLogin(code: String): LoginResponse {
        val kakaoToken = kakaoClientSecret.getKakaoAccessToken(code)
        val kakaoUserInfo = kakaoClientSecret.getKakaoUserInfo(kakaoToken.accessToken)

        val user = registerOrLogin(kakaoUserInfo, kakaoToken.refreshToken)
        return loginResponseMapper.toLoginResponse(user)
    }

    private fun registerOrLogin(kakaoUser: KakaoUserInfoDto, socialRefreshToken: String): User {
        val existingUser = userRepository.findByProviderId(kakaoUser.providerId)
        val user = userDomainService.decideRegisterOrUpdate(existingUser, kakaoUserInfo = kakaoUser, socialRefreshToken)
        return userRepository.save(user)
    }

}
