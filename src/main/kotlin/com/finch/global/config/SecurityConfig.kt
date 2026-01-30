package com.finch.global.config

import com.finch.global.exception.security.LoginRequiredEntryPoint
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val corsConfigurationSource: CorsConfigurationSource,
    private val loginRequiredEntryPoint: LoginRequiredEntryPoint // 주입
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            // CSRF 설정
            .csrf { it.disable() }
            // CORS 설정
            .cors { it.configurationSource(corsConfigurationSource) }

            // 세션 관리 (JWT를 사용할 것이므로 사용하지 않음)
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }

            // Form Login 및 HTTP Basic 비활성화
            .formLogin { it.disable() }
            .httpBasic { it.disable() }

            // 요청 권한 설정
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/health").permitAll()
                    .anyRequest().authenticated()
            }

            // Security 에서 걸린 애들 즉, authenticated()에 로그인을 안한 애들은 예외처리
            .exceptionHandling { ex ->
                ex.authenticationEntryPoint(loginRequiredEntryPoint)
            }

        return http.build()
    }

}