package com.finch.api.user.presentation.controller

import com.finch.api.user.application.port.`in`.SocialLoginUseCase
import com.finch.api.user.presentation.dto.request.AuthCodeRequest
import com.finch.api.user.presentation.dto.response.LoginResponse
import com.finch.global.common.domain.response.BaseResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    val socialLoginUseCase: SocialLoginUseCase
) {

    @PostMapping("/kakao/login")
    fun kakaoWebSocialLogin(
        @RequestBody request: AuthCodeRequest
    ): BaseResponse<LoginResponse> {
        return BaseResponse.ok(socialLoginUseCase.kakaoWebSocialLogin(request.code))
    }

}
