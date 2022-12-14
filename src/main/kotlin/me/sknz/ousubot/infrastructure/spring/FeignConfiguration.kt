package me.sknz.ousubot.infrastructure.spring

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import feign.Logger
import feign.RequestInterceptor
import feign.codec.Decoder
import feign.jackson.JacksonDecoder
import me.sknz.ousubot.app.api.adapter.OsuAuthenticationInterceptor
import me.sknz.ousubot.app.api.adapter.OsuTokenRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FeignConfiguration {

    @Bean
    fun feignLoggerLevel(): Logger.Level {
        return Logger.Level.BASIC
    }

    @Bean
    fun feignDecoder(objectMapper: ObjectMapper): Decoder {
        return JacksonDecoder(objectMapper)
    }

    @Bean
    fun objectMapper(): ObjectMapper {
        return ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .findAndRegisterModules()
    }
    @Bean
    fun osuAuthInterceptor(repository: OsuTokenRepository): RequestInterceptor {
        return OsuAuthenticationInterceptor(repository)
    }
}