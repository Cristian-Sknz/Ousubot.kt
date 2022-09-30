package me.sknz.ousubot.services

import me.sknz.ousubot.api.OsuClientAPI
import me.sknz.ousubot.dto.DiscordUserEmbed
import me.sknz.ousubot.dto.UserRequest
import org.thymeleaf.spring5.SpringTemplateEngine

/**
 * ## UserService
 *
 * Classe responsável pelas regras de negócio envolvendo requisições
 * de SlashCommands para a API de usuarios do Osu!
 *
 * @see me.sknz.ousubot.commands.UserController
 */
interface UserService<T>: CachedService<T> {

    val client: OsuClientAPI
    val engine: SpringTemplateEngine

    /**
     * Função para pegar/gerar uma mensagem prontas.
     *
     * Por baixo dos panos essa função utiliza de cache em memória
     * para otimizar o processamento de uma requisição e evitar
     * requisições desnecessárias na API do Osu
     *
     * @param request Uma requisição de beatmap ou beatmapSet
     */
    fun getUserService(request: UserRequest): DiscordUserEmbed
}