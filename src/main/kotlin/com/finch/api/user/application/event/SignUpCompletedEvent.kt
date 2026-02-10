package com.finch.api.user.application.event

import org.springframework.context.ApplicationEvent
import java.time.LocalDateTime

/**
 * 회원가입 완료 이벤트
 * 회원가입이 성공적으로 완료되었을 때 발행되는 이벤트
 */
class SignUpCompletedEvent(
    source: Any,
    val userId: Long,
    val email: String,
    val name: String,
    val signUpDateTime: LocalDateTime = LocalDateTime.now()
) : ApplicationEvent(source)
