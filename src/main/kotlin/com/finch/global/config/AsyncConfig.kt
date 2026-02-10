package com.finch.global.config

import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor

/**
 * 비동기 처리 설정
 * @Async 어노테이션을 사용하기 위한 설정
 */
@Configuration
@EnableAsync
class AsyncConfig : AsyncConfigurer {

    override fun getAsyncExecutor(): Executor {
        return ThreadPoolTaskExecutor().apply {
            corePoolSize = 5           // 기본 스레드 수
            maxPoolSize = 10           // 최대 스레드 수
            queueCapacity = 25         // 큐 크기
            setThreadNamePrefix("async-email-") // 스레드 이름 접두사
            initialize()
        }
    }
}
