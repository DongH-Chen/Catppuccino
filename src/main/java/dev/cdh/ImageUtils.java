package dev.cdh;

import java.awt.*;
import java.awt.image.BufferedImage;

public final class ImageUtils {
    private ImageUtils() {}

    public static BufferedImage flipHorizontally(BufferedImage source) {
        BufferedImage flipped = new BufferedImage(
                source.getWidth(),
                source.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g2d = flipped.createGraphics();
        g2d.drawImage(source, source.getWidth(), 0, -source.getWidth(), source.getHeight(), null);
        g2d.dispose();

        return flipped;
    }
}