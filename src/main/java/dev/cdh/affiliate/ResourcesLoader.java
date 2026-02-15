package dev.cdh.affiliate;

import dev.cdh.constants.Behave;
import dev.cdh.constants.BubbleState;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.random.RandomGenerator;

public final class ResourcesLoader {
    private static final List<String> CAT_TYPES = List.of("calico_cat", "grey_tabby_cat", "orange_cat", "white_cat");
    private final String selectedCatType;
    private static final RandomGenerator RAN = RandomGenerator.getDefault();

    public ResourcesLoader() {
        this.selectedCatType = CAT_TYPES.get(RAN.nextInt(CAT_TYPES.size()));
    }

    public List<BufferedImage> loadFrames(Behave behave) {
        return loadFrames(behave.name().toLowerCase(), behave.getFrame());
    }

    public List<BufferedImage> loadBubbleFrames(BubbleState state) {
        if (state == BubbleState.NONE) {
            return Collections.emptyList();
        }
        return loadFrames(state.name().toLowerCase(), state.getFrame());
    }

    private List<BufferedImage> loadFrames(String actionName, int frameCount) {
        List<BufferedImage> frames = new ArrayList<>(frameCount);

        for (int i = 1; i <= frameCount; i++) {
            String path = String.format("%s/%s/%s_%d.png",
                    selectedCatType, actionName, actionName, i);

            try (InputStream stream = getClass().getClassLoader().getResourceAsStream(path)) {
                BufferedImage image = ImageIO.read(Objects.requireNonNull(stream));
                frames.add(image);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load: " + path, e);
            }
        }

        return frames;
    }
}