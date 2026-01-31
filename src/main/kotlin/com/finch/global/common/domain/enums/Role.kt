package com.finch.global.common.domain.enums

enum class Role(
    val text: String
) {

    USER("회원"),
    PENDING("대기"),
    ADMIN("관리자")

}