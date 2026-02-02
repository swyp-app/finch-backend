package com.finch.api.user.domain.service

import com.finch.api.user.domain.entity.User
import com.finch.api.user.infrastructure.social.kakao.dto.KakaoUserInfoDto
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component

@Component
class UserDomainService {

    fun decideRegisterOrUpdate(
        existingUser: User?,
        kakaoUserInfo: KakaoUserInfoDto,
        socialRefreshToken: String
    ): User {
        return existingUser?.apply {
                log.info { "[SocialLogin] 기존 사용자 ${existingUser.name}님 로그인" }
                updateSocialRefreshToken(socialRefreshToken)
            }
            ?: User.createKakaoUserBuilder(kakaoUserInfo, socialRefreshToken).also {
                log.info { "[SocialLogin] 신규 사용자 회원가입" }
            }
    }

}

private val log = KotlinLogging.logger {}