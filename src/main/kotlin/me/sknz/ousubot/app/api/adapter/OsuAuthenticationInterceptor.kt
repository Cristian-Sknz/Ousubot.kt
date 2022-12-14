package me.sknz.ousubot.app.api.adapter

import feign.RequestInterceptor
import feign.RequestTemplate
import me.sknz.ousubot.app.api.OsuTokenScheduler
import me.sknz.ousubot.infrastructure.exceptions.osuInvalidApiKey
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
 * @see OsuTokenScheduler
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
            osuInvalidApiKey("Não existe nenhum código de acesso para a OsuAPI!")

        if (auth.expireDate?.isAfter(OffsetDateTime.now()) == true) {
            template.header("Authorization", "Bearer ${auth.accessToken}")
            template.decodeVariables()
            return
        }

        throw RuntimeException("Código de acesso está expirado ou invalido!")
    }

    /**
     * Substituir os caracteres de paramêtros que estão
     * codificados (encoded) pelos caracteres corretos.
     *
     * de ``myurl.com/%3Fname%3Dmyname``
     * para ``myurl.com/?name=myname``
     */
    private fun RequestTemplate.decodeVariables() {
        val url = this.request().url()
            .replace("%3F", "?")
            .replace("%26", "&")
            .replace("%3D", "=")

        this.uri(url)
    }
}