package me.sknz.ousubot.api.adapter

import feign.RequestInterceptor
import feign.RequestTemplate
import java.time.OffsetDateTime

/**
 * ## OsuAuthenticationInterceptor
 *
 * Classe responsável em assinar o header de autorização ('Authorization')
 * essa classe verifica se tem um token e faz uma verificação rasa
 * para ver se o token está valido [OsuAccessToken.expireDate]
 * ```
 * header("Authorization", "Bearer accessToken")
 * ```
 *
 * @see OsuAccessToken
 * @see me.sknz.ousubot.api.OsuTokenScheduler
 */
class OsuAuthenticationInterceptor(
    private val repository: OsuTokenRepository
) : RequestInterceptor {

    /**
     * Implementação da assinatura de header
     * e verificação de token
     *
     * @see OsuAuthenticationInterceptor
     * @throws RuntimeException caso não existir um token valido.
     */
    override fun apply(template: RequestTemplate) {
        val auth = repository.findAll().find { true } ?:
            throw RuntimeException("Não existe nenhum código de acesso para a OsuAPI!")

        if (auth.expireDate?.isAfter(OffsetDateTime.now()) == true) {
            template.header("Authorization", "Bearer ${auth.accessToken}")
            return
        }

        throw RuntimeException("Código de acesso está expirado ou invalido!")
    }
}