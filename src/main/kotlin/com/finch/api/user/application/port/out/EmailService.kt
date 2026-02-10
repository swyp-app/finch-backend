package com.finch.api.user.application.port.out

interface EmailService {

    /** 회원가입 웰컴 이메일 발송 */
    fun sendWelcomeEmail(toEmail: String, userName: String)

    /** 비밀번호 재설정 이메일 발송 */
    fun sendPasswordResetEmail(toEmail: String, resetToken: String)

    /** 일반 이메일 발송 (수신자, 제목, 내용) */
    fun sendEmail(toEmail: String, subject: String, content: String)
}