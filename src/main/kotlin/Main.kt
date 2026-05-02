package dev.cdh

import dev.cdh.affiliate.Cat
import dev.cdh.affiliate.CatController
import dev.cdh.affiliate.ResourcesLoader
import dev.cdh.affiliate.SystemTrayManager
import javax.swing.SwingUtilities

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        SwingUtilities.invokeLater {
            SystemTrayManager.initialize()
            val loader = ResourcesLoader()
            val cat = Cat(loader)
            val controller = CatController(cat)
            controller.start()
        }
    }
}