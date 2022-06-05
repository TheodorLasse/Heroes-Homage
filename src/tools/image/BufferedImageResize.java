package src.tools.image;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BufferedImageResize {
    /**
     * Resizes a BufferedImage
     * @param img image to resize
     * @param newW new width for image
     * @param newH new height for image
     * @return resized image
     */
    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
}
