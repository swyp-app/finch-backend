package com.finch.api.user.application.port.out

interface TokenProvider {
    fun createAccessToken(userId: Long, role: String): String
    fun createRefreshToken(userId: Long, role: String): String
}