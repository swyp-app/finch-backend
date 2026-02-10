package com.finch.api.user.application.event

import com.finch.api.user.application.port.out.EmailService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class SignUpEventListener(
    private val emailService: EmailService
) {

    /**
     * 회원가입 완료 시 웰컴 이메일 발송
     * @TransactionalEventListener: 트랜잭션 커밋 후 실행 (AFTER_COMMIT)
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleSignUpCompleted(event: SignUpCompletedEvent) {
        if (event.email.isBlank()) {
            logger.warn { "회원가입 웰컴 이메일 발송 건너뜀: 이메일 정보가 없습니다. - userId: ${event.userId}" }
            return
        }

        try {
            logger.info { "회원가입 웰컴 이메일 발송 시작 - userId: ${event.userId}, email: ${event.email}" }

            emailService.sendWelcomeEmail(
                toEmail = event.email,
                userName = event.name
            )

            logger.info { "회원가입 웰컴 이메일 발송 완료 - userId: ${event.userId}" }
        } catch (e: Exception) {
            logger.error(e) { "회원가입 웰컴 이메일 발송 실패 - userId: ${event.userId}, email: ${event.email}" }
        }
    }

}

private val logger = KotlinLogging.logger {}
