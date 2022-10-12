package me.sknz.ousubot.infrastructure.events.commands

import me.sknz.ousubot.infrastructure.annotations.commands.SlashCommandController
import me.sknz.ousubot.infrastructure.events.commands.autocomplete.NumberAutoComplete
import me.sknz.ousubot.infrastructure.events.commands.autocomplete.StringAutoComplete
import me.sknz.ousubot.infrastructure.events.commands.custom.CustomSlashCommandData
import me.sknz.ousubot.infrastructure.events.commands.custom.RunnableCommand
import me.sknz.ousubot.infrastructure.events.commands.tools.CommandParameterInjector
import me.sknz.ousubot.infrastructure.exceptions.AbstractExceptionHandler
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.SubscribeEvent
import net.dv8tion.jda.api.requests.RestAction
import org.apache.logging.log4j.LogManager
import org.springframework.context.ApplicationContext
import java.lang.reflect.InvocationTargetException

/**
 * SlashCommands responsável em armazenar e registrar comandos.
 * Está classe é uma classe interna e não deve ser modificada externamente!
 *
 * Essa classe é chamada ao configurar o [net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder]
 * em [me.sknz.ousubot.infrastructure.JDAConfiguration]
 *
 * @param context Contexto da aplicação Spring Boot
 * @see SlashCommands
 */
internal class SlashCommands(
    private val context: ApplicationContext,
) {
    private val injector = CommandParameterInjector(context)
    private val factory = CommandFunctionFactory()
    private val commands: HashMap<String, CustomSlashCommandData> = hashMapOf()
    private val logger = LogManager.getLogger(this::class.java)

    @SubscribeEvent
    fun slashCommand(event: SlashCommandInteractionEvent) {
        if (event.name.lowercase() !in this.commands) {
            event.reply("Este comando não está disponível no bot.")
                .setEphemeral(true)
                .queue()
            return
        }

        val command: RunnableCommand? = event.subcommandName.let {
            val cmd = this.commands[event.name.lowercase()]!!
            if (it != null) {
                return@let cmd.getSubcommand(it)
            }
            return@let cmd
        }

        try {
            command?.let { runnable ->
                logger.info("Executando um comando '${event.commandPath}': ${event.options.map {"${it.name}=${it.asString}"}}")
                val params = injector.getInitializedParameters(event, command.function.function)
                val callback = runnable.call(*params.toTypedArray())
                if (callback is RestAction<*>) callback.queue()
            }
        } catch (exception: InvocationTargetException) {
            context.getBeansOfType(AbstractExceptionHandler::class.java).values.forEach {
                it.onException(event, exception.targetException)
            }
        }
    }

    @SubscribeEvent
    fun autocomplete(event: CommandAutoCompleteInteractionEvent) {
        val value = this.commands[event.name.lowercase()] ?: return
        val focused = event.focusedOption

        val complete = event.subcommandName.let {
            if (it == null) {
                return@let value.getOption(focused.name)
                    ?.getAutoComplete(context)
            }
            return@let value.getSubcommand(event.subcommandName!!)
                ?.getOption(focused.name)
                ?.getAutoComplete(context)
        }

        complete?.let {
            if (it is StringAutoComplete || it is NumberAutoComplete) {
                event.replyChoices(complete.onAutoComplete(focused.value)
                    .map { value -> value.toCommandChoice() })
                    .queue()
            }
        }
    }

    @SubscribeEvent
    fun messageInteraction(event: MessageContextInteractionEvent) {
        val command = this.commands[event.name.lowercase()] ?: return

        try {
            command.let {
                val params = injector.getInitializedParameters(event, command.function.function)
                val callback = it.call(*params.toTypedArray())
                if (callback is RestAction<*>) callback.queue()
            }
        } catch (exception: InvocationTargetException) {
            context.getBeansOfType(AbstractExceptionHandler::class.java).values.forEach {
                it.onException(event, exception.targetException)
            }
        }
    }

    /**
     * Função para registrar comando(s) de uma instância de um [SlashCommandController].
     * Está função por baixo dos panos, utiliza reflexão para manipular as anotações
     *
     * Esta função também registra SubComandos [SlashCommandController.name]
     *
     * @param instance Uma classe instanciada de [SlashCommandController]
     * @see SlashCommandController
     */
    fun register(instance: Any) {
        val commands = factory.create(instance, instance::class)
        commands.forEach { this.commands[it.name.lowercase()] = it }
        logger.info("Foram registrados ${commands.sumOf { it.subcommands.size.coerceAtLeast(1) }} comandos em ${instance::class.simpleName}")
    }

    /**
     * Função para atualizar os comandos registrados nesta classe e
     * enviar para a API do Discord.
     *
     * Esta função ira apagar todos os comandos que não estão inclusos nesta classe
     * e utiliza o [JDA.upsertCommand] para registrar novos comandos.
     *
     * @param jda Uma instância do [JDA]
     * @see JDA.upsertCommand
     */
    fun update(jda: JDA) {
        jda.retrieveCommands().complete().forEach {
            if (!commands.containsKey(it.name)) it.delete().complete()
        }
        commands.values.forEach {
            jda.upsertCommand(it).queue()
        }
    }
}





