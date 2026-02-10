package com.finch.global.exception.handleException

import com.finch.global.exception.enums.ErrorCode

open class ServiceException (
    val errorCode: ErrorCode,
    cause: Throwable? = null
) : RuntimeException(errorCode.message, cause)

/** Global (공통) */
class ServerException : ServiceException(ErrorCode.INTERNAL_SERVER_ERROR)
class InvalidAuthorizationException : ServiceException(ErrorCode.INVALID_AUTHORIZATION_CODE)
class InvalidAuthProviderException : ServiceException(ErrorCode.INVALID_AUTH_PROVIDER_EXCEPTION)

/** 카카오 */
class KakaoTokenIssueFailedException : ServiceException(ErrorCode.KAKAO_TOKEN_ISSUE_FAILED)
class KakaoInvalidTokenResponseException : ServiceException(ErrorCode.KAKAO_INVALID_TOKEN_RESPONSE)
class KakaoUserInfoRetrieveFailedException : ServiceException(ErrorCode.KAKAO_USER_INFO_RETRIEVE_FAILED)
class KakaoInvalidUserResponseException : ServiceException(ErrorCode.KAKAO_INVALID_USER_RESPONSE)

/** 애플 */
class InvalidApplePrivateKeyException : ServiceException(ErrorCode.APPLE_INVALID_PRIVATE_KEY)
class AppleTokenIssueFailedException : ServiceException(ErrorCode.APPLE_TOKEN_ISSUE_FAILED)
class AppleInvalidTokenResponseException : ServiceException(ErrorCode.APPLE_INVALID_TOKEN_RESPONSE)
class AppleTokenMalformedException : ServiceException(ErrorCode.APPLE_TOKEN_MALFORMED)

/** 구글 */
class GoogleTokenIssueFailedException : ServiceException(ErrorCode.GOOGLE_TOKEN_ISSUE_FAILED)
class GoogleInvalidTokenResponseException : ServiceException(ErrorCode.GOOGLE_INVALID_TOKEN_RESPONSE)
class GoogleInvalidUserResponseException : ServiceException(ErrorCode.GOOGLE_USER_INFO_RETRIEVE_FAILED)

/** User */
class DuplicateEmailException : ServiceException(ErrorCode.DUPLICATE_EMAIL_EXCEPTION)
class UserNotFoundException : ServiceException(ErrorCode.USER_NOT_FOUND)
class InvalidPasswordException : ServiceException(ErrorCode.INVALID_PASSWORD)
class LocalLoginOnlyException : ServiceException(ErrorCode.LOCAL_LOGIN_ONLY)

