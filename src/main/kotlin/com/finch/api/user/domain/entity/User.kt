package com.finch.api.user.domain.entity

import com.finch.api.user.infrastructure.social.apple.dto.AppleUserInfoDto
import com.finch.api.user.infrastructure.social.google.dto.GoogleUserInfoDto
import com.finch.api.user.infrastructure.social.kakao.dto.KakaoUserInfoDto
import com.finch.api.user.presentation.dto.request.SignUpRequest
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
import org.springframework.security.crypto.password.PasswordEncoder

@Entity
@Table(name = "users")
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(unique = true)
    val email: String? = null,

    @Column(name = "password")
    var password: String? = null,

    @Column(nullable = false)
    var name: String,

    @Column(name = "profile_image_url")
    var profileImageUrl: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val provider: Provider,

    @Column(name = "provider_id", nullable = false)
    val providerId: String?,

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

    /** 폼 로그인용 비밀번호 검증 */
    fun verifyPassword(
        rawPassword: String,
        passwordEncoder: PasswordEncoder
    ): Boolean {
        return password != null && passwordEncoder.matches(rawPassword, password)
    }

    companion object {
        fun createKakaoUserBuilder(kakaoUser: KakaoUserInfoDto, socialRefreshToken: String): User {
            return User(
                id = 0L,
                email = kakaoUser.email,
                password = "",
                name = kakaoUser.name ?: "카카오 사용자",
                profileImageUrl = kakaoUser.profileImageUrl,
                providerId = kakaoUser.providerId,
                provider = Provider.KAKAO,
                socialRefresh = socialRefreshToken,
                currency = Currency.KRW,
                role = Role.PENDING
            )
        }

        fun createAppleUserBuilder(appleUser: AppleUserInfoDto, socialRefreshToken: String): User {
            return User(
                id = 0L,
                email = appleUser.email ?: "",
                password = "",
                name = "애플 사용자",
                profileImageUrl = "",
                providerId = appleUser.providerId,
                provider = Provider.APPLE,
                socialRefresh = socialRefreshToken,
                currency = Currency.KRW,
                role = Role.PENDING
            )
        }

        fun createGoogleUserBuilder(googleUser: GoogleUserInfoDto, socialRefreshToken: String): User {
            return User(
                id = 0L,
                email = googleUser.email,
                password = "",
                name = googleUser.name ?: "구글 사용자",
                profileImageUrl = googleUser.picture ?: "",
                providerId = googleUser.providerId,
                provider = Provider.GOOGLE,
                socialRefresh = socialRefreshToken,
                currency = Currency.KRW,
                role = Role.PENDING
            )
        }

        fun createFormUserBuilder(request: SignUpRequest, encodedPassword: String): User {
            return User(
                id = 0L,
                email = request.email,
                password = encodedPassword,
                name = request.name,
                profileImageUrl = "",
                providerId = "",
                provider = Provider.LOCAL,
                socialRefresh = "",
                currency = Currency.KRW,
                role = Role.PENDING
            )
        }
    }
}