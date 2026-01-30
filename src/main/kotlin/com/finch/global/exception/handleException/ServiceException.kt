package com.finch.global.exception.handleException

import com.finch.global.exception.enums.ErrorCode

open class ServiceException (
    val errorCode: ErrorCode,
    cause: Throwable? = null
) : RuntimeException(errorCode.message, cause)

// 구체적인 예외 상황
class ServerException : ServiceException(ErrorCode.INTERNAL_SERVER_ERROR)