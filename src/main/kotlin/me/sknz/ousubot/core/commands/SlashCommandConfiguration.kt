package me.sknz.ousubot.core.commands

import me.sknz.ousubot.core.context.SlashCommandContext
import org.springframework.aop.framework.ProxyFactoryBean
import org.springframework.aop.target.ThreadLocalTargetSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Scope

@Configuration
class SlashCommandConfiguration {

    @Bean(destroyMethod = "destroy")
    fun threadLocalTargetSource(): ThreadLocalTargetSource {
        val result = ThreadLocalTargetSource()
        result.targetBeanName = "slashCommandContext"
        return result
    }

    @Bean(name = ["slashCommandContext"])
    @Scope(scopeName = "prototype")
    fun slashCommandContext(): SlashCommandContext? {
        return null
    }


    @Primary
    @Bean(name = ["proxiedThreadLocalTargetSource"])
    fun proxiedThreadLocalTargetSource(threadLocal: ThreadLocalTargetSource): ProxyFactoryBean {
        val result = ProxyFactoryBean()
        result.targetSource = threadLocal
        return result
    }
}