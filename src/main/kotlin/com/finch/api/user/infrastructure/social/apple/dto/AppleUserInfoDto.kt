package com.finch.api.user.infrastructure.social.apple.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

data class AppleUserInfoDto(
    val providerId: String,
    val email: String?
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class AppleTokenPayload(
        val sub: String,
        val email: String?
    )

    companion object {
        fun of(payload: AppleTokenPayload): AppleUserInfoDto {
            return AppleUserInfoDto(
                providerId = payload.sub,
                email = payload.email
            )
        }
    }
}