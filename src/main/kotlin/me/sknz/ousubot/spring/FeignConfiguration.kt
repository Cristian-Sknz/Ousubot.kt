package me.sknz.ousubot.spring

import com.fasterxml.jackson.databind.ObjectMapper
import feign.codec.Decoder
import feign.jackson.JacksonDecoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FeignConfiguration {

    @Bean
    fun feignDecoder(objectMapper: ObjectMapper): Decoder {
        return JacksonDecoder()
    }

    @Bean
    fun objectMapper(): ObjectMapper {
        return ObjectMapper()
    }
}