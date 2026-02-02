package com.finch.api.user.infrastructure.social.kakao.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoTokenDto(
    @get:JsonProperty("access_token")
    val accessToken: String,

    @get:JsonProperty("token_type")
    val tokenType: String,

    @get:JsonProperty("refresh_token")
    val refreshToken: String,

    @get:JsonProperty("expires_in")
    val expiresIn: Long,

    @get:JsonProperty("scope")
    val scope: String? = null,

    @get:JsonProperty("refresh_token_expires_in")
    val refreshTokenExpiresIn: Long
)