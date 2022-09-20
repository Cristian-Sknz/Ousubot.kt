package me.sknz.ousubot.interactions

import me.sknz.ousubot.api.OsuTokenScheduler
import me.sknz.ousubot.api.adapter.OsuTokenClient
import me.sknz.ousubot.api.adapter.OsuTokenRepository
import me.sknz.ousubot.core.annotations.modal.ModalController
import me.sknz.ousubot.core.annotations.modal.ModalHandler
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.requests.RestAction

@ModalController
class MiscModalController(
    private val tokenClient: OsuTokenClient,
    private val tokenScheduler: OsuTokenScheduler,
    private val repository: OsuTokenRepository
) {

    @ModalHandler(id = "osu-token")
    fun token(event: ModalInteractionEvent): RestAction<*> {
        val token = event.getValue("token")!!.asString

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