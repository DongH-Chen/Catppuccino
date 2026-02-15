package dev.cdh;

import dev.cdh.affiliate.Cat;
import dev.cdh.affiliate.CatController;
import dev.cdh.affiliate.ResourcesLoader;
import dev.cdh.affiliate.SystemTrayManager;

import javax.swing.*;

public final class Main {

    static void main() {
        SwingUtilities.invokeLater(() -> {
            SystemTrayManager.initialize();
            ResourcesLoader resourcesLoader = new ResourcesLoader();
            Cat cat = new Cat(resourcesLoader);
            CatController controller = new CatController(cat);
            controller.start();
        });
    }
}