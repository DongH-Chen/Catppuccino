package dev.cdh;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ImageCache {
    private static final Map<String, List<BufferedImage>> FRAME_CACHE = new HashMap<>();
    private static final Map<String, BufferedImage> FLIP_CACHE = new HashMap<>();

    public static List<BufferedImage> getOrLoadFrames(String key, FrameLoader loader) {
        return FRAME_CACHE.computeIfAbsent(key, _ -> loader.load());
    }

    public static BufferedImage getOrFlip(BufferedImage original, String key) {
        return FLIP_CACHE.computeIfAbsent(key, _ -> flipImage(original));
    }

    private static BufferedImage flipImage(BufferedImage source) {
        BufferedImage flipped = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = flipped.createGraphics();
        g2d.drawImage(source, source.getWidth(), 0, -source.getWidth(), source.getHeight(), null);
        g2d.dispose();
        return flipped;
    }

    @FunctionalInterface
    public interface FrameLoader {
        List<BufferedImage> load();
    }
}