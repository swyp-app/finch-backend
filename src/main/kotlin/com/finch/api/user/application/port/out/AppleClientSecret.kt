package com.finch.api.user.application.port.out

interface AppleClientSecret {
    
    /** 애플 클라이언트 시크릿 키 생성 */
    fun createAppleClientSecret(): String
}