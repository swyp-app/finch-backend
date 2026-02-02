package com.finch.api.user.infrastructure.social.kakao.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoUserResponse(
    val id: Long,
    @JsonProperty("kakao_account")
    val kakaoAccount: KakaoAccount?
) {
    data class KakaoAccount(
        val email: String?,
        val profile: KakaoUserProfile?
    ) {
        data class KakaoUserProfile(
            val nickname: String?,
            @JsonProperty("profile_image_url")
            val profileImageUrl: String?
        )
    }
}
