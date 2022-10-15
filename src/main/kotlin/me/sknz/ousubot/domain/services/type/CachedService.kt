package me.sknz.ousubot.domain.services.type

import org.springframework.cache.annotation.Cacheable

/**
 * ## CachedService
 *
 * ‘Interface’ para declarar um serviço que terá regras de negócio envolvendo requisições
 * de SlashCommands com cache em memória.
 *
 */
interface CachedService<T> {

    /**
     * Pegar a instância desta classe como proxy.
     *
     * A anotação [Cacheable] funciona apenas na instância com proxy,
     * se for utilizado o `this` o caching não irã funcionar.
     */
    var self: T
}