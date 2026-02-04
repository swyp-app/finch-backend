package com.finch.api.user.infrastructure.social.apple.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.finch.api.user.domain.service.SocialUserInfoDto

data class AppleUserInfoDto(
    override val providerId: String,
    override val email: String?
) : SocialUserInfoDto {
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