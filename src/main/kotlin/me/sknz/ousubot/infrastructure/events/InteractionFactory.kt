package me.sknz.ousubot.infrastructure.events

import kotlin.reflect.KClass

/**
 * ## InteractionFactory
 *
 * ‘Interface’ responsável em criar manipuladores de interações do Discord,
 * A partir de uma classe instanciada.
 *
 * Cada função de uma classe instanciada irá se tornar um manipulador de uma interação.
 */
interface InteractionFactory<T> {

    /**
     *  Função responsável em criar interações a partir de funções de uma classe
     *  instanciada.
     *
     *  @param instance Uma classe instanciada
     *  @param clazz A classe desta instancia
     *  @param params Parametros extras para a função
     *
     */
    fun create(instance: Any, clazz: KClass<out Any>, vararg params: Any?): List<T>
}