package me.sknz.ousubot.core

import me.sknz.ousubot.api.OsuTokenScheduler
import me.sknz.ousubot.core.annotations.commands.SlashCommandController
import me.sknz.ousubot.core.annotations.modal.ModalController
import me.sknz.ousubot.core.commands.SlashCommands
import me.sknz.ousubot.core.modal.ModalInteractions
import net.dv8tion.jda.api.hooks.AnnotatedEventManager
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder
import net.dv8tion.jda.api.sharding.ShardManager
import net.dv8tion.jda.api.utils.ChunkingFilter
import net.dv8tion.jda.api.utils.MemberCachePolicy
import net.dv8tion.jda.api.utils.cache.CacheFlag
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * ## Configuração do JDA
 *
 * Esta classe irá cuidar da configuração inicial do JDA
 * também será inicializado a conexão com o WebSocket.
 *
 * @see net.dv8tion.jda.api.JDA
 * @see net.dv8tion.jda.api.sharding.ShardManager
 */
@Configuration
class JDAConfiguration {

    @Bean
    fun getDefaultShardBuilder(
        @Value("\${application.discord.token}") token: String,
        @Value("\${application.discord.shards}") shards: String,
    ): DefaultShardManagerBuilder {
        val builder = DefaultShardManagerBuilder.createDefault(token)
        builder.setEventManagerProvider { AnnotatedEventManager() }
        builder.setShardsTotal(shards.toIntOrNull() ?: 1)

        return builder
    }

    fun configureMemoryUsage(builder: DefaultShardManagerBuilder) {
        builder.disableCache(CacheFlag.ACTIVITY)
        builder.setMemberCachePolicy(MemberCachePolicy.VOICE.or(MemberCachePolicy.OWNER))
        builder.setChunkingFilter(ChunkingFilter.NONE)
        builder.disableIntents(GatewayIntent.GUILD_MESSAGE_TYPING)
        builder.setLargeThreshold(50)
    }

    @Bean
    fun start(context: ApplicationContext, builder: DefaultShardManagerBuilder): ShardManager {
        val commands = context.getBeansWithAnnotation(SlashCommandController::class.java)
        val modals = context.getBeansWithAnnotation(ModalController::class.java)
        val tokenScheduler = context.getBean(OsuTokenScheduler::class.java)

        val slashCommands = SlashCommands(context)
        val modalInteractions = ModalInteractions()
        builder.addEventListeners(slashCommands)
        builder.addEventListeners(modalInteractions)

        for ((_, value) in modals) {
            modalInteractions.register(value)
        }

        for ((_, value) in commands) {
            slashCommands.register(value)
        }
        tokenScheduler.schedule()
        val shardManager = builder.build()

        for (shard in shardManager.shards) {
            slashCommands.update(shard)
        }

        return shardManager
    }
}