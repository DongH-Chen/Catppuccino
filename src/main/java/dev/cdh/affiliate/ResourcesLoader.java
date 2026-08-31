package dev.cdh.affiliate;

import dev.cdh.ImageCache;
import dev.cdh.constants.Behave;
import dev.cdh.constants.BubbleState;
import dev.cdh.skin.CatSkin;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class ResourcesLoader {
    private final String selectedCatType;

    /**
     * @param skin 皮肤描述，其 {@link CatSkin#resourceRoot()} 作为资源根目录
     */
    public ResourcesLoader(CatSkin skin) {
        this.selectedCatType = Objects.requireNonNull(skin).resourceRoot();
    }

    public String catType() {
        return selectedCatType;
    }

    public List<BufferedImage> loadFrames(Behave behave) {
        String cacheKey = selectedCatType + ":" + behave.name();
        return ImageCache.getOrLoadFrames(cacheKey,
                () -> loadFramesInternal(behave.name().toLowerCase(), behave.frame(), CatWindow.WINDOW_SIZE));
    }

    public List<BufferedImage> loadBubbleFrames(BubbleState state) {
        if (state == BubbleState.NONE) {
            return Collections.emptyList();
        }
        String cacheKey = "bubble:" + state.name();
        return ImageCache.getOrLoadFrames(cacheKey,
                () -> loadFramesInternal(state.name().toLowerCase(), state.frame(), CatWindow.BUBBLE_SIZE));
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
