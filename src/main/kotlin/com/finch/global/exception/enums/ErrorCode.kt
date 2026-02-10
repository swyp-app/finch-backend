package com.finch.global.exception.enums

enum class ErrorCode(
    val httpStatus: Int,
    val code: String,
    val message: String
) {

    /** Global (공통) */
    INTERNAL_SERVER_ERROR(500, "G002", "서버 내부 오류가 발생했습니다."),
    INVALID_AUTHORIZATION_CODE(400, "INVALID_AUTHORIZATION_CODE", "유효하지 않은 인가 코드입니다."),
    INVALID_AUTH_PROVIDER_EXCEPTION(400, "INVALID_AUTH_PROVIDER_EXCEPTION", "지원하지 않는 소셜 로그인 수단입니다."),

    /** KaKao Social Login */
    KAKAO_TOKEN_ISSUE_FAILED(502, "KAKAO_TOKEN_ISSUE_FAILED", "카카오 토큰 발급 서버에 문제가 발생했습니다."),
    KAKAO_INVALID_TOKEN_RESPONSE(400, "KAKAO_INVALID_TOKEN_RESPONSE", "카카오 토큰 발급 응답이 올바르지 않습니다."),
    KAKAO_USER_INFO_RETRIEVE_FAILED(502, "KAKAO_USER_INFO_RETRIEVE_FAILED", "카카오 유저 정보 조회 중 서버에 문제가 발생했습니다."),
    KAKAO_INVALID_USER_RESPONSE(400, "KAKAO_INVALID_USER_RESPONSE", "카카오 유저 정보를 불러오는데 실패했습니다."),

    /** Apple Social Login */
    APPLE_INVALID_PRIVATE_KEY(500, "APPLE_INVALID_PRIVATE_KEY", "Apple Private Key 생성에 실패했습니다. 키 형식을 확인하세요."),
    APPLE_TOKEN_ISSUE_FAILED(502, "APPLE_TOKEN_ISSUE_FAILED", "애플 토큰 발급 서버에 문제가 발생했습니다."),
    APPLE_INVALID_TOKEN_RESPONSE(400, "APPLE_INVALID_TOKEN_RESPONSE", "애플 토큰 발급 응답이 올바르지 않습니다."),
    APPLE_TOKEN_MALFORMED(400, "APPLE_TOKEN_MALFORMED", "애플 idToken 토큰 형식이 잘못 되었습니다."),

    /** Google Social Login */
    GOOGLE_TOKEN_ISSUE_FAILED(502, "GOOGLE_TOKEN_ISSUE_FAILED", "구글 토큰 발급 서버에 문제가 발생했습니다."),
    GOOGLE_INVALID_TOKEN_RESPONSE(400, "GOOGLE_INVALID_TOKEN_RESPONSE", "구글 토큰 발급 응답이 올바르지 않습니다."),
    GOOGLE_USER_INFO_RETRIEVE_FAILED(400, "GOOGLE_USER_INFO_RETRIEVE_FAILED", "구글 유저 정보를 불러오는데 실패했습니다."),

    /** User */
    DUPLICATE_EMAIL_EXCEPTION(401, "DUPLICATE_EMAIL_EXCEPTION", "증복된 이메일입니다."),
    USER_NOT_FOUND(404, "USER_NOT_FOUND", "존재하지 않는 사용자입니다."),
    INVALID_PASSWORD(401, "INVALID_PASSWORD", "비밀번호가 올바르지 않습니다."),
    LOCAL_LOGIN_ONLY(400, "LOCAL_LOGIN_ONLY", "소셜 로그인으로 가입된 계정입니다. 소셜 로그인을 이용해 주세요."),
}