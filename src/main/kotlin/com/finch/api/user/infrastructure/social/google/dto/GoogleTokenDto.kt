package com.finch.api.user.infrastructure.social.google.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class GoogleTokenDto(

    @get:JsonProperty("access_token")
    val accessToken: String?,

    @get:JsonProperty("expires_in")
    val expiresIn: Int?,

    @get:JsonProperty("scope")
    val scope: String?,

    @get:JsonProperty("token_type")
    val tokenType: String?,

    @get:JsonProperty("id_token")
    val idToken: String?,

    @get:JsonProperty("refresh_token")
    val refreshToken: String?
)