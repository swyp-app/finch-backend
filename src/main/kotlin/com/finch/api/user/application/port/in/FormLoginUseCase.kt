package com.finch.api.user.application.port.`in`

import com.finch.api.user.presentation.dto.request.SignUpRequest

interface FormLoginUseCase {
    
    /** 회원가입 */
    fun signUp(request: SignUpRequest): String
}