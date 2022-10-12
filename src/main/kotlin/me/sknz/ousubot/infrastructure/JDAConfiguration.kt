package me.sknz.ousubot.infrastructure

import me.sknz.ousubot.app.api.OsuTokenScheduler
import me.sknz.ousubot.infrastructure.annotations.commands.SlashCommandController
import me.sknz.ousubot.infrastructure.annotations.interaction.InteractionController
import me.sknz.ousubot.infrastructure.annotations.interaction.InteractionType
import me.sknz.ousubot.infrastructure.events.commands.SlashCommands
import me.sknz.ousubot.infrastructure.events.interactions.impl.ButtonInterations
import me.sknz.ousubot.infrastructure.events.interactions.impl.ModalInteractions
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
import kotlin.reflect.full.findAnnotation

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
        val interactions = context.getBeansWithAnnotation(InteractionController::class.java)
        val tokenScheduler = context.getBean(OsuTokenScheduler::class.java)

        val slashCommands = builder.addListener(SlashCommands(context))
        val modalInteractions = builder.addListener(ModalInteractions())
        val buttonInteractions = builder.addListener(ButtonInterations())

        for ((_, value) in interactions.byType(InteractionType.MODAL)) {
            modalInteractions.register(value)
        }

        for ((_, value) in interactions.byType(InteractionType.BUTTON)) {
            buttonInteractions.register(value)
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

    private fun Map<String, Any>.byType(type: InteractionType): Map<String, Any> {
        return this.filter { it.value::class.findAnnotation<InteractionController>()!!.type == type}
    }

    private fun <T> DefaultShardManagerBuilder.addListener(any: T): T {
        this.addEventListeners(any)
        return any
    }
}