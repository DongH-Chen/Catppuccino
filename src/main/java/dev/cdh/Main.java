package dev.cdh;

import dev.cdh.affiliate.Cat;
import dev.cdh.affiliate.CatController;
import dev.cdh.affiliate.ResourcesLoader;
import dev.cdh.affiliate.SystemTrayManager;
import dev.cdh.skin.SkinRegistry;

import javax.swing.*;

public final class Main {

    static void main() {
        SwingUtilities.invokeLater(() -> {
            SystemTrayManager.initialize();
            SkinRegistry registry = SkinRegistry.load();
            ResourcesLoader resourcesLoader = new ResourcesLoader(registry.randomSkin());
            Cat cat = new Cat(resourcesLoader);
            CatController controller = new CatController(cat);
            controller.start();
        });
    }
}