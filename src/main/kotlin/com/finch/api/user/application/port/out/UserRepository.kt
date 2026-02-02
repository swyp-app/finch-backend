package com.finch.api.user.application.port.out

import com.finch.api.user.domain.entity.User

interface UserRepository {
    
    /** providerId로 회원 정보 조회 */
    fun findByProviderId(providerId: String): User?
    
    /** User Entity 저장 */
    fun save(user: User): User

}