package src.tools.image;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class BufferedImageRotate {
    /**
     * Rotates an image
     * @param rotation Radians of rotation
     * @param image Image to rotate
     * @param at AffineTransform object
     * @return Rotated image
     */
    public static BufferedImage rotateImage(double rotation, BufferedImage image, AffineTransform at) {
        final double sin = Math.abs(Math.sin(rotation));
        final double cos = Math.abs(Math.cos(rotation));
        final int w = (int) Math.floor(image.getWidth() * cos + image.getHeight() * sin);
        final int h = (int) Math.floor(image.getHeight() * cos + image.getWidth() * sin);
        final BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
        at.translate((double) w / 2, (double) h / 2);
        at.rotate(rotation, 0, 0);
        at.translate((double) -image.getWidth() / 2, (double) -image.getHeight() / 2);
        final AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        rotateOp.filter(image, rotatedImage);
        return rotatedImage;
    }
}
