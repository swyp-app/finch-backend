package com.finch.api.user.application.port.`in`

import com.finch.api.user.presentation.dto.request.LoginRequest
import com.finch.api.user.presentation.dto.request.SignUpRequest
import com.finch.api.user.presentation.dto.response.LoginResponse

interface FormLoginUseCase {
    
    /** 회원가입 */
    fun signUp(request: SignUpRequest): String
    
    /** 로그인 */
    fun login(request: LoginRequest): LoginResponse
}