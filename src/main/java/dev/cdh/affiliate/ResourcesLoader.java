package dev.cdh.affiliate;

import dev.cdh.ImageCache;
import dev.cdh.constants.Behave;
import dev.cdh.constants.BubbleState;
import dev.cdh.constants.Layout;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.random.RandomGenerator;

public final class ResourcesLoader {
    private static final List<String> CAT_TYPES = List.of("calico_cat", "grey_tabby_cat", "orange_cat", "white_cat");
    private static final RandomGenerator RAN = RandomGenerator.getDefault();

    private final String selectedCatType;

    public ResourcesLoader() {
        this.selectedCatType = CAT_TYPES.get(RAN.nextInt(CAT_TYPES.size()));
    }

    public String catType() {
        return selectedCatType;
    }

    public List<BufferedImage> loadFrames(Behave behave) {
        String cacheKey = selectedCatType + ":" + behave.name();
        return ImageCache.getOrLoadFrames(cacheKey,
                () -> loadFramesInternal(behave.name().toLowerCase(), behave.frame(), Layout.WINDOW_SIZE));
    }

    public List<BufferedImage> loadBubbleFrames(BubbleState state) {
        if (state == BubbleState.NONE) {
            return Collections.emptyList();
        }
        String cacheKey = "bubble:" + state.name();
        return ImageCache.getOrLoadFrames(cacheKey,
                () -> loadFramesInternal(state.name().toLowerCase(), state.frame(), Layout.BUBBLE_SIZE));
    }

    private List<BufferedImage> loadFramesInternal(String actionName, int frameCount, int targetSize) {
        List<BufferedImage> frames = new ArrayList<>(frameCount);
        String basePath = selectedCatType + "/" + actionName;
        for (int i = 1; i <= frameCount; i++) {
            String path = String.format("%s/%s_%d.png", basePath, actionName, i);
            frames.add(prepareImage(loadImage(path), targetSize));
        }
        return frames;
    }

    /**
     * 统一转换为 ARGB 并预缩放到目标尺寸，避免每帧绘制时重复缩放。
     * 使用最近邻插值以保持原有像素风外观。
     */
    private BufferedImage prepareImage(BufferedImage image, int targetSize) {
        if (image.getType() == BufferedImage.TYPE_INT_ARGB
                && image.getWidth() == targetSize
                && image.getHeight() == targetSize) {
            return image;
        }
        BufferedImage prepared = new BufferedImage(targetSize, targetSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = prepared.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2d.drawImage(image, 0, 0, targetSize, targetSize, null);
        g2d.dispose();
        return prepared;
    }

    private BufferedImage loadImage(String path) {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(path)) {
            return ImageIO.read(Objects.requireNonNull(stream, "Missing resource: " + path));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load: " + path, e);
        }
    }
}
