package src.tools;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.EnumMap;
import java.util.Map;

public class ImageLoader
{
    public enum ImageName
    {
        GRASS, WATER, ROCK, BLUE_FLAG, RED_FLAG, MENU_BACKGROUND, GAME_BORDER, GAME_BORDER_CORNER, RESOURCE_PANEL, ERROR
    }

    /**
     * From enum to actual file name
     */
    final private static Map<ImageName, String> IMAGE_NAME_MAP = Map.ofEntries(
            Map.entry(ImageName.GRASS, "grass"),
            Map.entry(ImageName.ROCK, "rock"),
            Map.entry(ImageName.BLUE_FLAG, "blue_flag"),
            Map.entry(ImageName.RED_FLAG, "red_flag"),
            Map.entry(ImageName.WATER, "water"),
            Map.entry(ImageName.MENU_BACKGROUND, "menu_background"),
            Map.entry(ImageName.GAME_BORDER, "game_border"),
            Map.entry(ImageName.GAME_BORDER_CORNER, "game_border_corner"),
            Map.entry(ImageName.RESOURCE_PANEL, "resource_panel"),

            Map.entry(ImageName.ERROR, "grass")
    );

    private final Map<ImageName, BufferedImage> images;

    public ImageLoader() {
        images = new EnumMap<>(ImageName.class);
    }

    /**
     * Returns the image with a given name.
     *
     * @param imgName Image name to look for.
     *
     * @return An Image
     */
    public BufferedImage getImage(ImageName imgName) {
        return images.get(imgName);
    }

    /**
     * Loads all images and creates sheets.
     */
    public void loadImages() throws IOException, FileNotFoundException {
        for (ImageName iterImageName : ImageName.values()) {
            final String name = "images/" + IMAGE_NAME_MAP.get(iterImageName) + ".png";
            final URL imgURL = ClassLoader.getSystemResource(name);

            if (imgURL == null) {
                throw new FileNotFoundException("Could not find resource " + name);
            }

            images.put(iterImageName, loadImage(imgURL));
        }
    }

    /**
     * Loads and returns an image. If the image can't be loaded, a default image will be returned.
     *
     * @param url
     *
     * @return image
     * @throws IOException
     */
    private static BufferedImage loadImage(URL url) throws IOException {
        return ImageIO.read(url);
    }

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
