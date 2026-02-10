package com.finch.api.user.application.service.command

import com.finch.api.user.application.event.SignUpCompletedEvent
import com.finch.api.user.application.mapper.LoginResponseMapper
import com.finch.api.user.application.port.`in`.FormLoginUseCase
import com.finch.api.user.application.port.out.UserRepository
import com.finch.api.user.domain.entity.User.Companion.createFormUserBuilder
import com.finch.api.user.presentation.dto.request.LoginRequest
import com.finch.api.user.presentation.dto.request.SignUpRequest
import com.finch.api.user.presentation.dto.response.LoginResponse
import com.finch.global.common.domain.enums.Provider
import com.finch.global.exception.handleException.DuplicateEmailException
import com.finch.global.exception.handleException.InvalidPasswordException
import com.finch.global.exception.handleException.LocalLoginOnlyException
import com.finch.global.exception.handleException.UserNotFoundException
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.transaction.Transactional
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class FormLoginService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val eventPublisher: ApplicationEventPublisher,
    private val loginResponseMapper: LoginResponseMapper
) : FormLoginUseCase {

    @Transactional
    override fun signUp(request: SignUpRequest): String {
        if (userRepository.existsByEmail(request.email)) {
            throw DuplicateEmailException()
        }

        val userBuilder = createFormUserBuilder(request, passwordEncoder.encode(request.password))
        val savedUser = userRepository.save(userBuilder)

        //  트랜잭션 커밋 후 이벤트 리스너가 비동기로 이메일 발송
        eventPublisher.publishEvent(
            SignUpCompletedEvent(
                source = this,
                userId = savedUser.id!!,
                email = savedUser.email ?: "",
                name = savedUser.name
            )
        )

        return "회원가입이 완료되었습니다."
    }

    override fun login(request: LoginRequest): LoginResponse {
        val user = userRepository.findByEmailAndProvider(request.email, Provider.LOCAL)
            ?: throw UserNotFoundException()

        if (user.provider != Provider.LOCAL) throw LocalLoginOnlyException()
        if (!user.verifyPassword(request.password, passwordEncoder)) throw InvalidPasswordException()

        return loginResponseMapper.toLoginResponse(user)
    }

}
