package com.finch.global.exception.handleException

import com.finch.global.common.domain.response.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException::class)
    protected fun handleServiceException(e: ServiceException): ResponseEntity<ErrorResponse> {
        val errorCode = e.errorCode
        val status = HttpStatus.valueOf(errorCode.httpStatus)

        return ResponseEntity
            .status(status)
            .body(ErrorResponse.of(status, errorCode))
    }

    @ExceptionHandler(Exception::class)
    protected fun handleException(e: Exception): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "생각지 못한 서버 내부 오류가 발생했습니다."))
    }
}