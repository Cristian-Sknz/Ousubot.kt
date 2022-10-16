package me.sknz.ousubot.infrastructure.tools

import me.skiincraft.ousucanvas.ImageBuilder
import me.skiincraft.ousucanvas.elements.Element
import me.skiincraft.ousucanvas.elements.ElementContainer
import me.skiincraft.ousucanvas.text.TextElement
import java.awt.image.BufferedImage

/**
 * ##  KotlinImageBuilder
 *
 * Classe que adapta o [ImageBuilder] para ser utilizado
 * com o DSL do Kotlin.
 *
 * @see ImageBuilder
 */
class KotlinImageBuilder(width: Int,
                         height: Int,
                         type: Int = BufferedImage.TYPE_INT_RGB
): ImageBuilder(width, height, type) {

    fun dsl(dsl: KotlinImageBuilder.() -> Unit): BufferedImage {
        this.apply(dsl)
        return this.toImage()
    }

    fun element(element: Element, container: ElementContainer.() -> Unit) {
        this.addElement(ElementContainer(element, 0, 0).apply(container).update())
    }

    fun element(container: ElementContainer.() -> Unit) {
        this.addElement(ElementContainer(EmptyElement, 0, 0).apply(container).update())
    }

    object EmptyElement: Element {
        override fun getWidth(): Int = 0
        override fun getHeight(): Int = 0
        override fun getElements() = emptyList<ElementContainer>()
    }

    fun ElementContainer.text(container: TextElement.() -> Unit) {
        this.element = TextElement("").apply(container)
        this.update()
    }

    fun ElementContainer.update(): ElementContainer {
        if (this.element == null) return this
        this.width = element.width
        this.height = element.height
        return this
    }
}