package me.sknz.ousubot.api.adapter

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "application.osu")
class OsuOAuthCredentials {
    lateinit var oauth: Map<String, String>
}