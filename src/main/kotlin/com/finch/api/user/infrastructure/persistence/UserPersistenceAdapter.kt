package com.finch.api.user.infrastructure.persistence

import com.finch.api.user.application.port.out.UserRepository
import com.finch.api.user.domain.entity.User
import org.springframework.stereotype.Service

@Service
class UserPersistenceAdapter(
    val userJpaRepository: UserJpaRepository
): UserRepository {

    override fun findByProviderId(providerId: String): User? {
        return userJpaRepository.findByProviderId(providerId)
    }

    override fun save(user: User): User {
        return userJpaRepository.save(user)
    }


}