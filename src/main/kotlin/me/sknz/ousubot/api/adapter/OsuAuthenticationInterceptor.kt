package me.sknz.ousubot.api.adapter

import feign.RequestInterceptor
import feign.RequestTemplate
import me.sknz.ousubot.api.annotations.WorkInProgress
import java.time.OffsetDateTime

@WorkInProgress
class OsuAuthenticationInterceptor(
    private val repository: OsuTokenRepository
) : RequestInterceptor {

    override fun apply(template: RequestTemplate) {
        val auth = repository.findAll().find { true }
        auth?.let {
            if (auth.expireDate?.isAfter(OffsetDateTime.now()) == true) {
                template.header("Authorization", "Bearer ${auth.accessToken}")
            }
        }
    }
}