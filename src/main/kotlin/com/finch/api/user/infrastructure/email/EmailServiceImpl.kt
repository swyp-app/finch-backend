package com.finch.api.user.infrastructure.email

import com.finch.api.user.application.port.out.EmailService
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.mail.MessagingException
import jakarta.mail.internet.MimeMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

@Service
class EmailServiceImpl(
    private val mailSender: JavaMailSender,
    private val templateEngine: TemplateEngine
) : EmailService {

    override fun sendWelcomeEmail(toEmail: String, userName: String) {
        val context = Context().apply {
            setVariable("userName", userName)
        }

        val htmlContent = templateEngine.process("email/welcome", context)
        sendEmail(toEmail, "🎉 StockPin에 오신 것을 환영합니다!", htmlContent)
    }

    override fun sendPasswordResetEmail(toEmail: String, resetToken: String) {
        val context = Context().apply {
            setVariable("resetToken", resetToken)
            setVariable("resetUrl", "https://finch.com/reset-password?token=$resetToken")
        }

        val htmlContent = templateEngine.process("email/password-reset", context)
        sendEmail(toEmail, "🔑 비밀번호 재설정 안내", htmlContent)
    }

    override fun sendEmail(toEmail: String, subject: String, content: String) {
        try {
            val message: MimeMessage = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(message, true, "UTF-8")

            helper.setTo(toEmail)
            helper.setSubject(subject)
            helper.setText(content, true)
            helper.setFrom("noreply@finch.com")

            mailSender.send(message)
            logger.info { "이메일 발송 성공 - to: $toEmail, subject: $subject" }
        } catch (e: MessagingException) {
            logger.error(e) { "이메일 발송 실패 - to: $toEmail, subject: $subject" }
            throw RuntimeException("이메일 발송에 실패했습니다.", e)
        }
    }
}

private val logger = KotlinLogging.logger {}
