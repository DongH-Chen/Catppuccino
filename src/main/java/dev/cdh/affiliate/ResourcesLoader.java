package dev.cdh.affiliate;

import dev.cdh.ImageCache;
import dev.cdh.constants.Behave;
import dev.cdh.constants.BubbleState;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;
import java.util.random.RandomGenerator;

public final class ResourcesLoader {
    private static final List<String> CAT_TYPES = List.of("calico_cat", "grey_tabby_cat", "orange_cat", "white_cat");
    private final String selectedCatType;
    private static final RandomGenerator RAN = RandomGenerator.getDefault();
    private final Map<String, List<BufferedImage>> localCache = new HashMap<>();

    public ResourcesLoader() {
        this.selectedCatType = CAT_TYPES.get(RAN.nextInt(CAT_TYPES.size()));
    }

    public List<BufferedImage> loadFrames(Behave behave) {
        String cacheKey = selectedCatType + ":" + behave.name();
        List<BufferedImage> frames = localCache.get(cacheKey);
        // Check local cache
        if (frames != null) {
            return frames;
        }
        // then check global cache
        frames = ImageCache.getOrLoadFrames(cacheKey, () -> loadFramesInternal(behave.name().toLowerCase(), behave.getFrame()));
        // Store into local cache
        localCache.put(cacheKey, frames);
        return frames;
    }

    public List<BufferedImage> loadBubbleFrames(BubbleState state) {
        if (state == BubbleState.NONE) {
            return Collections.emptyList();
        }
        String cacheKey = "bubble:" + state.name();
        List<BufferedImage> frames = localCache.get(cacheKey);
        if (frames != null) {
            return frames;
        }
        frames = ImageCache.getOrLoadFrames(cacheKey, () -> {
            String actionName = state.name().toLowerCase();
            return loadFramesInternal(actionName, state.getFrame());
        });
        localCache.put(cacheKey, frames);
        return frames;
    }

    private List<BufferedImage> loadFramesInternal(String actionName, int frameCount) {
        List<BufferedImage> frames = new ArrayList<>(frameCount);
        String basePath = selectedCatType + "/" + actionName;
        for (int i = 1; i <= frameCount; i++) {
            String path = String.format("%s/%s_%d.png", basePath, actionName, i);
            BufferedImage image = loadImage(path);
            if (image.getType() != BufferedImage.TYPE_INT_ARGB) {
                BufferedImage converted = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = converted.createGraphics();
                g2d.drawImage(image, 0, 0, null);
                g2d.dispose();
                image = converted;
            }
            frames.add(image);
        }
        return frames;
    }

    private BufferedImage loadImage(String path) {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(path)) {
            if (stream == null) {
                throw new RuntimeException("Resource not found: " + path);
            }
            return ImageIO.read(Objects.requireNonNull(stream));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load: " + path, e);
        }
    }
}