package dev.cdh.affiliate;

import dev.cdh.Animate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.random.RandomGenerator;

public final class ResourcesLoader {
    private static final List<String> catType = List.of("calico_cat", "grey_tabby_cat", "orange_cat", "white_cat");
    private static final RandomGenerator ran = RandomGenerator.getDefault();
    private static final String selectedCatType = catType.get(ran.nextInt(0, 4));

    public static <T extends Enum<T> & Animate> Map<String, List<BufferedImage>> loadAllFrames(Class<T> clazz) {
        Map<String, List<BufferedImage>> container = new HashMap<>();
        EnumSet<T> enumSet = EnumSet.allOf(clazz);
        for (T entry : enumSet) {
            container.put(entry.name(), loadFrames(entry));
        }
        return container;
    }

    private static <T extends Enum<T> & Animate> List<BufferedImage> loadFrames(T entry) {
        return loadFrame(entry.name(), entry.getFrame());
    }

    private static List<BufferedImage> loadFrame(String actionName, int frameCount) {
        ArrayList<BufferedImage> imgContainer = new ArrayList<>();
        for (int i = 1; i <= frameCount; i++) {
            String format = String.format("%s/%s/%s_%d.png", selectedCatType, actionName.toLowerCase(), actionName.toLowerCase(), i);
            try (InputStream stream = ResourcesLoader.class.getClassLoader().getResourceAsStream(format)) {
                BufferedImage image = ImageIO.read(Objects.requireNonNull(stream));
                imgContainer.add(image);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        imgContainer.trimToSize();
        return imgContainer;
    }

    private ResourcesLoader() throws IllegalAccessException {
        throw new IllegalAccessException("Can not be initialize!");
    }
}