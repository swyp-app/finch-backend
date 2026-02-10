package com.finch.api.user.infrastructure.persistence

import com.finch.api.user.domain.entity.User
import com.finch.global.common.domain.enums.Provider
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserJpaRepository : JpaRepository<User, Long> {

    fun findByProviderId(providerId: String): User?

    fun findByEmailAndProvider(email: String, provider: Provider): User?

    fun existsByEmail(email: String): Boolean
}