package com.finch.api.user.domain.entity

import com.finch.api.user.infrastructure.social.kakao.dto.KakaoUserInfoDto
import com.finch.global.common.domain.enums.Currency
import com.finch.global.common.domain.enums.Provider
import com.finch.global.common.domain.enums.Role
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(unique = true)
    val email: String?,

    @Column(nullable = false)
    var name: String,

    @Column(name = "profile_image_url")
    var profileImageUrl: String?,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val provider: Provider,

    @Column(name = "provider_id", nullable = false)
    val providerId: String,

    @Column(name = "social_refresh")
    var socialRefresh: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var currency: Currency = Currency.KRW,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: Role = Role.PENDING
){

    fun updateSocialRefreshToken(newToken: String?) {
        if (newToken.isNullOrBlank()) return
        this.socialRefresh = newToken
    }

    companion object {
        fun createKakaoUserBuilder(kakaoUser: KakaoUserInfoDto, socialRefreshToken: String): User {
            return User(
                id = 0L,
                email = kakaoUser.email,
                name = kakaoUser.name ?: "카카오 사용자",
                profileImageUrl = kakaoUser.profileImageUrl,
                providerId = kakaoUser.providerId,
                provider = Provider.KAKAO,
                socialRefresh = socialRefreshToken,
                currency = Currency.KRW,
                role = Role.PENDING
            )
        }
    }
}