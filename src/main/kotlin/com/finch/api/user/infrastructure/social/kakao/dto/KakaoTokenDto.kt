package com.finch.api.user.infrastructure.social.kakao.dto

import com.finch.api.user.domain.service.SocialUserInfoDto

data class KakaoUserInfoDto(
    override val providerId: String,
    override val email: String?,
    val name: String?,
    val profileImageUrl: String?
) : SocialUserInfoDto {
    companion object {
        fun of(
            providerId: Long,
            kakaoAccount: KakaoUserResponse.KakaoAccount,
            profile: KakaoUserResponse.KakaoAccount.KakaoUserProfile
        ): KakaoUserInfoDto {
            return KakaoUserInfoDto(
                providerId = providerId.toString(),
                email = kakaoAccount.email,
                name = profile.nickname,
                profileImageUrl = profile.profileImageUrl
            )
        }
    }
}