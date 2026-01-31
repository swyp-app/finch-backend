package com.finch.api.user.domain.entity

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
    val id: Long? = null,

    @Column(unique = true)
    val email: String?,

    @Column(nullable = false)
    var name: String,

    @Column(name = "profile_image_url")
    var profileImageUrl: String,

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

}