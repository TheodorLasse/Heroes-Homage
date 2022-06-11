package src.tools.image;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BufferedImageMirror {
    public static BufferedImage mirrorHorizontally(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D gg = newImage.createGraphics();
        gg.drawImage(image, image.getWidth(), 0, -image.getWidth(), image.getHeight(), null);
        gg.dispose();
        return newImage;
    }
}
