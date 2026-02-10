package com.finch.api.user.infrastructure.persistence

import com.finch.api.user.application.port.out.UserRepository
import com.finch.api.user.domain.entity.User
import com.finch.global.common.domain.enums.Provider
import org.springframework.stereotype.Service

@Service
class UserPersistenceAdapter(
    val userJpaRepository: UserJpaRepository
): UserRepository {

    override fun findByProviderId(providerId: String): User? {
        return userJpaRepository.findByProviderId(providerId)
    }

    override fun findByEmailAndProvider(email: String, provider: Provider): User? {
        return userJpaRepository.findByEmailAndProvider(email, provider)
    }

    override fun save(user: User): User {
        return userJpaRepository.save(user)
    }

    override fun existsByEmail(email: String): Boolean {
        return userJpaRepository.existsByEmail(email)
    }

}