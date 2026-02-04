package com.finch.api.user.domain.service

import com.finch.api.user.domain.entity.User
import com.finch.api.user.infrastructure.social.apple.dto.AppleUserInfoDto
import com.finch.api.user.infrastructure.social.kakao.dto.KakaoUserInfoDto
import com.finch.global.common.domain.enums.Provider
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component

@Component
class UserDomainService {

    fun decideRegisterOrUpdate(
        existingUser: User?,
        userInfo: SocialUserInfoDto,
        socialRefreshToken: String,
        provider: Provider
    ): User {
        return existingUser?.apply {
            log.info { "[SocialLogin] 기존 사용자 ${name}님 로그인 (${provider})" }
            updateSocialRefreshToken(socialRefreshToken)
        } ?: createUserByProvider(userInfo, socialRefreshToken, provider).also {
            log.info { "[SocialLogin] 신규 사용자 회원가입 (${provider})" }
        }
    }

    private fun createUserByProvider(info: SocialUserInfoDto, token: String, provider: Provider): User {
        return when (provider) {
            Provider.KAKAO -> User.createKakaoUserBuilder(info as KakaoUserInfoDto, token)
            Provider.APPLE -> User.createAppleUserBuilder(info as AppleUserInfoDto, token)
        }
    }

}

private val log = KotlinLogging.logger {}