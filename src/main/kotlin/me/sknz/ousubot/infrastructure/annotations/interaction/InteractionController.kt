package me.sknz.ousubot.infrastructure.annotations.interaction

import org.springframework.stereotype.Component
import java.lang.annotation.Inherited

/**
 * Anotação para declarar uma classe como um controlador de modais
 * Ao declarar uma classe como InteractionController, ela começa a se comportar
 * como uma rota para eventos envolvendo interações do Discord.
 *
 * Se a propriedade [InteractionController.id] não for vazia, esta classe irá se comportar como um
 * grupo de interações com vários sub-controladores
 * ```
 * @ModalController(type=InteractionType.MODAL, id="admin")
 * class MyAwesomeInteraction {
 *
 *      // id será formado como `admin:ban`
 *      @InteractionHandler(id="ban")
 *      fun ban() {}
 *
 *      // id será formado como `admin:mute` e assim por diante
 *      @InteractionHandler(id="mute")
 *      fun mute() {}
 * }
 * ```
 * @see InteractionHandler
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Component
@Inherited
annotation class InteractionController(
    /**
     * Ao preencher esta propriedade a classe que for instanciada com
     * está anotação irá se comportar como um grupo de interações.
     */
    val type: InteractionType,
    val id: String = ""
)