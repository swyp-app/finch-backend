package com.finch.api.user.infrastructure.social.persistence

import com.finch.api.user.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserJpaRepository : JpaRepository<User, Long> {

    fun findByProviderId(providerId: String): User?
}