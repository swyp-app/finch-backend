package com.finch.global.exception.enums

enum class ErrorCode(
    val httpStatus: Int,
    val code: String,
    val message: String
) {

    // Global (공통)
    INTERNAL_SERVER_ERROR(500, "G002", "서버 내부 오류가 발생했습니다.");
}