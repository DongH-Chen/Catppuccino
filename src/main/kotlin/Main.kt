package dev.cdh

import dev.cdh.affiliate.Cat
import dev.cdh.affiliate.CatController
import dev.cdh.affiliate.ResourcesLoader
import dev.cdh.affiliate.SystemTrayManager
import dev.cdh.affiliate.initialize
import dev.cdh.skin.SkinRegistry
import javax.swing.SwingUtilities

fun main() {
    SwingUtilities.invokeLater {
        SystemTrayManager.initialize()
        val registry = SkinRegistry.load()
        val resourcesLoader = ResourcesLoader(registry.randomSkin())
        val cat = Cat(resourcesLoader)
        val controller = CatController(cat)
        controller.start()
    }
}