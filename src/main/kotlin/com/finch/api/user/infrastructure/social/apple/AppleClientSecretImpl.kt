package com.finch.api.user.infrastructure.social.apple

import com.finch.api.user.application.port.out.AppleClientSecret
import com.finch.global.exception.handleException.InvalidApplePrivateKeyException
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.Base64
import java.util.Date

@Component
class AppleClientSecretImpl(
    @Value("\${APPLE_TEAM_ID}")
    private val teamId: String,

    @Value("\${APPLE_KEY_ID}")
    private val keyId: String,

    @Value("\${APPLE_CLIENT_ID}")
    private val clientId: String,

    @Value("\${APPLE_PRIVATE_KEY}")
    private val privateKeyP8: String

): AppleClientSecret {

    override fun createAppleClientSecret(): String {
        val now = Date()
        val expiration = Date(now.time + 3600000)

        return Jwts.builder()
            .header()
            .keyId(keyId).and()          // kid
            .issuer(teamId)              // iss
            .audience().add("https://appleid.apple.com").and() // aud
            .subject(clientId)          // sub
            .issuedAt(now)               // iat
            .expiration(expiration)           // exp
            .signWith(getPrivateKey()) // 서명
            .compact()
    }


    private fun getPrivateKey(): PrivateKey {
        return try {
            val privateKeyContent = privateKeyP8
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("\\s+".toRegex(), "")

            // Base64 디코딩
            val encoded = Base64.getDecoder().decode(privateKeyContent)

            // KeyFactory를 이용해 PrivateKey 객체 생성 (Apple은 EC 알고리즘 사용)
            val keySpec = PKCS8EncodedKeySpec(encoded)
            val kf = KeyFactory.getInstance("EC")

            kf.generatePrivate(keySpec)
        } catch (e: Exception) {
            throw InvalidApplePrivateKeyException()
        }
    }

}