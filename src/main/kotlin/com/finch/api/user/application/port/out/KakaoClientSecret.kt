package com.finch.api.user.application.port.out

import com.finch.api.user.infrastructure.social.kakao.dto.KakaoTokenDto
import com.finch.api.user.infrastructure.social.kakao.dto.KakaoUserInfoDto

interface KakaoClientSecret {

    /** 카카오 서버로 부터 인증 토큰 받기 */
    fun getKakaoAccessToken(code: String): KakaoTokenDto

    /** 토큰으로 카카오 회원 정보 받기 */
    fun getKakaoUserInfo(accessToken: String): KakaoUserInfoDto
}