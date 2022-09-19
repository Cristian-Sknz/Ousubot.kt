package me.sknz.ousubot.core.annotations.modal

import org.springframework.stereotype.Component
import java.lang.annotation.Inherited

/**
 * Anotação para declarar uma classe como um controlador de modais
 * Ao declarar uma classe como ModalController, ela começa a se comportar
 * como uma rota para eventos envolvendo modais do Discord.
 *
 * Se a propriedade [ModalController.id] não for vazia, está classe irá se comportar como um
 * controlador de grupos de modais
 * ```
 * @ModalController(id="admin")
 * class MyAwesomeCommand {
 *
 *      // id será formado como `admin:ban`
 *      @ModalHandler(id="ban")
 *      fun ban() {}
 *
 *      // id será formado como `admin:mute` e assim por diante
 *      @ModalHandler(name="ban")
 *      fun mute() {}
 * }
 * ```
 * @see ModalHandler
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Component
@Inherited
annotation class ModalController (
    /**
     * Ao preencher esta propriedade a classe que for instanciada com
     * está anotação irá se comportar como um grupo de modais.
     */
    val id: String = ""
)