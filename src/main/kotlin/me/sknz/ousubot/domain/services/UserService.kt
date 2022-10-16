package me.sknz.ousubot.domain.services

import me.sknz.ousubot.app.commands.UserController
import me.sknz.ousubot.domain.dto.DiscordUserEmbed
import me.sknz.ousubot.domain.dto.UserRequest
import me.sknz.ousubot.domain.services.type.CachedService
import me.sknz.ousubot.domain.services.type.OsuAPIService

/**
 * ## UserService
 *
 * Classe responsável pelas regras de negócio envolvendo requisições
 * de SlashCommands para a API de usuarios do Osu!
 *
 * @see UserController
 */
interface UserService<T: UserService<T>>: CachedService<T>, OsuAPIService {

    /**
     * Função para pegar/gerar uma mensagem prontas.
     *
     * Por baixo dos panos essa função utiliza de cache em memória
     * para otimizar o processamento de uma requisição e evitar
     * requisições desnecessárias na API do Osu
     *
     * @param request Uma requisição de usuario
     */
    fun getUserEmbed(request: UserRequest): DiscordUserEmbed
}