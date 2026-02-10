package com.finch.api.user.application.port.out

import com.finch.api.user.domain.entity.User
import com.finch.global.common.domain.enums.Provider

interface UserRepository {
    
    /** providerId로 회원 정보 조회 */
    fun findByProviderId(providerId: String): User?

    /** email + provider로 회원 정보 조회 */
    fun findByEmailAndProvider(email: String, provider: Provider): User?
    
    /** User Entity 저장 */
    fun save(user: User): User
    
    /** Email 있는지 여부 확인 */
    fun existsByEmail(email: String): Boolean

}