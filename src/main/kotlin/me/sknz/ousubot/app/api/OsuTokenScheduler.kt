package me.sknz.ousubot.app.api

import me.sknz.ousubot.app.api.adapter.OsuTokenClient
import me.sknz.ousubot.app.api.adapter.OsuTokenRepository
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.OffsetDateTime
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

/**
 * OsuTokenScheduler é uma classe responsável por renovar o código de autenticação
 * da OsuAPI.
 *
 * A renovação é feita com um agendamento de uma tarefa que irá fazer uma chamada
 * assíncrona para receber o novo código de acesso. Este processo será repetido
 * indefinidamente até a aplicação ser desligada
 *
 * A Tarefa poderá ser cancelada usando a função [OsuTokenScheduler.cancel].
 */
@Component
class OsuTokenScheduler(
    private val repository: OsuTokenRepository,
    private val tokenClient: OsuTokenClient
) {

    private val executor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
    private var task: ScheduledFuture<*>? = null

    /**
     * Função para iniciar o agendamento de uma renovação de código de acesso.
     * Basicamente, esta função irá usar a data de expiração e a data atual
     * para pegar o tempo restante para agendar a tarefa.
     *
     * Se o token já estiver expirado essa tarefa irá ser disparada
     * imediatamente!
     */
    fun schedule() {
        val value = repository.findById(1).orElse(null) ?: return
        val delay = Duration.between(OffsetDateTime.now(), value.expireDate)

        val runnable = Runnable {
            val call = tokenClient.getRenewedToken(value.refreshToken!!)

            val response = call.execute()
            if (response.isSuccessful) {
                repository.save(tokenClient.getAccessToken(response))
                return@Runnable
            }
            repository.deleteById(1)
        }

        if (task != null) cancel()

        if (delay.seconds < 1) {
            runnable.run()
            return
        }

        task = executor.schedule(runnable, delay.seconds, TimeUnit.SECONDS)
    }

    /**
     * Função para cancelar a tarefa agendada
     * pela função [OsuTokenScheduler.schedule]
     */
    fun cancel() = task?.cancel(true)
}