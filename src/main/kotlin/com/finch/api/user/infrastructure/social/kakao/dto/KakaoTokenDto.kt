package com.finch.api.user.infrastructure.social.kakao.dto

data class KakaoUserInfoDto(
    val providerId: String,
    val email: String?,
    val name: String?,
    val profileImageUrl: String?
){
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