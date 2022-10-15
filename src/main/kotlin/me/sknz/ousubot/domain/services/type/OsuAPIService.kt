package me.sknz.ousubot.domain.services.type

import me.sknz.ousubot.app.api.OsuClientAPI
import org.thymeleaf.spring5.SpringTemplateEngine

/**
 * ## CachedService
 *
 * ‘Interface’ para declarar um serviço que terá regras de negócio envolvendo requisições
 * de SlashCommands utilizando a API do Osu
 *
 * @see OsuClientAPI
 */
interface OsuAPIService {

    /**
     * Uma instância do Client para a API do Osu
     */
    val client: OsuClientAPI

    /**
     * Engine Template para gerar as mensagens
     */
    val engine: SpringTemplateEngine
}