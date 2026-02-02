package com.finch.global.exception.handleException

import com.finch.global.exception.enums.ErrorCode

open class ServiceException (
    val errorCode: ErrorCode,
    cause: Throwable? = null
) : RuntimeException(errorCode.message, cause)

// 구체적인 예외 상황
class ServerException : ServiceException(ErrorCode.INTERNAL_SERVER_ERROR)
class InvalidAuthorizationException : ServiceException(ErrorCode.INVALID_AUTHORIZATION_CODE)
class KakaoTokenIssueFailedException : ServiceException(ErrorCode.KAKAO_TOKEN_ISSUE_FAILED)
class KakaoInvalidTokenResponseException : ServiceException(ErrorCode.KAKAO_INVALID_TOKEN_RESPONSE)
class KakaoUserInfoRetrieveFailedException : ServiceException(ErrorCode.KAKAO_USER_INFO_RETRIEVE_FAILED)
class KakaoInvalidUserResponseException : ServiceException(ErrorCode.KAKAO_INVALID_USER_RESPONSE)