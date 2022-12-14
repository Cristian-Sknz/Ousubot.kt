package me.sknz.ousubot.app.interactions

import me.sknz.ousubot.app.api.OsuTokenScheduler
import me.sknz.ousubot.app.api.adapter.OsuTokenClient
import me.sknz.ousubot.app.api.adapter.OsuTokenRepository
import me.sknz.ousubot.infrastructure.annotations.interaction.InteractionController
import me.sknz.ousubot.infrastructure.annotations.interaction.InteractionHandler
import me.sknz.ousubot.infrastructure.annotations.interaction.InteractionType
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.requests.RestAction

@InteractionController(type = InteractionType.MODAL)
class MiscModalController(
    private val tokenClient: OsuTokenClient,
    private val tokenScheduler: OsuTokenScheduler,
    private val repository: OsuTokenRepository
) {

    @InteractionHandler(id = "osu-token")
    fun token(event: ModalInteractionEvent): RestAction<*> {
        val token = event.getValue("token")!!.asString
        if (token.length < 80) {
            return event.reply("Digite um Token valido!").setEphemeral(true)
        }

        event.deferReply(true).complete().let {
            val response = tokenClient.getToken(token).execute()
            if (response.isSuccessful) {
                tokenScheduler.cancel()
                repository.save(tokenClient.getAccessToken(response))
                tokenScheduler.schedule()
                return it.sendMessage("Concluído com sucesso!").setEphemeral(true)
            }

            return it.sendMessage("Ocorreu um erro ao tentar obter um token!\n ${response.message}")
        }
    }
}