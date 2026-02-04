package com.finch.api.user.infrastructure.social.apple.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class AppleTokenResponse(
    @JsonProperty("access_token")
    val accessToken: String,

    @JsonProperty("expires_in")
    val expiresIn: Int,

    @JsonProperty("id_token")
    val idToken: String, // 여기에 유저 정보가 들어있음

    @JsonProperty("refresh_token")
    val refreshToken: String, // 꼭 DB에 저장해야 함 (나중에 탈퇴시 필요)

    @JsonProperty("token_type")
    val tokenType: String
)