package com.finch.api.user.presentation.controller

import com.finch.api.user.application.port.`in`.FormLoginUseCase
import com.finch.api.user.application.port.`in`.SocialLoginUseCase
import com.finch.api.user.presentation.dto.request.AuthCodeRequest
import com.finch.api.user.presentation.dto.request.KakaoAppAuthRequest
import com.finch.api.user.presentation.dto.request.LoginRequest
import com.finch.api.user.presentation.dto.request.SignUpRequest
import com.finch.api.user.presentation.dto.response.LoginResponse
import com.finch.global.common.domain.response.BaseResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class UserController(
    private val socialLoginUseCase: SocialLoginUseCase,
    private val formLoginUseCase: FormLoginUseCase
) {

    @PostMapping("/kakao/login/web")
    fun kakaoWebSocialLogin(
        @RequestBody request: AuthCodeRequest
    ): BaseResponse<LoginResponse> {
        return BaseResponse.ok(socialLoginUseCase.kakaoWebSocialLogin(request.code))
    }

    @PostMapping("/kakao/login/app")
    fun kakaoAppSocialLogin(
        @RequestBody request: KakaoAppAuthRequest
    ): BaseResponse<LoginResponse> {
        return BaseResponse.ok(socialLoginUseCase.kakaoAppSocialLogin(request))
    }

    @PostMapping("/apple/login/app")
    fun appleAppSocialLogin(
        @RequestBody request: AuthCodeRequest
    ): BaseResponse<LoginResponse> {
        return BaseResponse.ok(socialLoginUseCase.appleAppSocialLogin(request.code))
    }

    @PostMapping("/apple/login/web")
    fun appleWebSocialLogin(
        @RequestBody request: AuthCodeRequest
    ): BaseResponse<LoginResponse> {
        return BaseResponse.ok(socialLoginUseCase.appleWebSocialLogin(request.code))
    }

    @PostMapping("/google/login/web")
    fun googleLogin(
        @RequestBody request: AuthCodeRequest
    ): BaseResponse<LoginResponse> {
        return BaseResponse.ok(socialLoginUseCase.googleSocialLogin(request.code))
    }

    @PostMapping("/signUp")
    fun signUp(
        @RequestBody request: SignUpRequest
    ): BaseResponse<String> {
        return BaseResponse.ok(formLoginUseCase.signUp(request))
    }

    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginRequest
    ): BaseResponse<LoginResponse> {
        return BaseResponse.ok(formLoginUseCase.login(request))
    }

}
