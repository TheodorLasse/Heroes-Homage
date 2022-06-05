package src.sprites;

import src.tools.*;


import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Class representing a texture that can draw itself but is not an Entity, i.e not an object that interacts physically with other objects.
 * Examples: background, healthbars, score etc.
 */
public class SpriteTexture implements Sprite
{
    private Vector2D position;
    private Vector2D size = new Vector2D();
    private Rotation rotation;
    private SpriteType spriteType;
    private BufferedImage image = null;
    private Color color = null;
    private String textString = null;
    private int fontSize = DEFAULT_FONT_SIZE;
    private final static int DEFAULT_FONT_SIZE = 24;

    /**
     * Create SpriteTexture with image.
     */
    public SpriteTexture(Vector2D position, double rotation, BufferedImage image) {
		this.spriteType = SpriteType.IMAGE;
		this.position = position;
		this.rotation = new Rotation(rotation);
		if(rotation != 0){
			this.image = ImageLoader.rotateImage(rotation, image, new AffineTransform());
		}else this.image = image;
		this.size = new Vector2D(position.getX() + image.getWidth(null), position.getY() + image.getHeight(null));
    }

    /**
     * Create SpriteTexture with size and color.
     */
    public SpriteTexture(Vector2D position, Vector2D size, double rotation, Color color, SpriteType spriteType) {
		this.spriteType = spriteType;
		this.color = color;
		this.position = position;
		this.size = size;
		this.rotation = new Rotation(rotation);

    }

    /**
     * Create SpriteTexture with text.
     */
    public SpriteTexture(Vector2D position, double rotation, Color color, String textString, int fontSize) {
		this.spriteType = SpriteType.TEXT;
		this.color = color;
		this.textString = textString;
		this.position = position;
		this.fontSize = fontSize;
		this.rotation = new Rotation(rotation);

    }

    /**
     * Create SpriteTexture with text and color.
     */
    public SpriteTexture(Vector2D position, double rotation, Color color, String textString) {
		this.spriteType = SpriteType.TEXT;
		this.color = color;
		this.textString = textString;
		this.position = position;
		this.rotation = new Rotation(rotation);
    }

    @Override public double getRotation() {
	return rotation.getRadians();
    }

    @Override public Vector2D getPosition() {
	return position;
    }

    @Override public Vector2D getSize() {
	return size;
    }

    @Override public void draw(final Graphics g, final JComponent gc) {
		int positionX = (int) position.getX();
		int positionY = (int) position.getY();
		switch (spriteType) {
			case IMAGE -> g.drawImage(image, positionX, positionY, gc);
			case LINE -> {
			g.setColor(color);
			g.drawLine(positionX, positionY, (int) size.getX(), (int) size.getY());
			}
			case TEXT -> {
			Font font = new Font("Verdana", Font.BOLD, fontSize);
			g.setFont(font);
			g.setColor(color);
			g.drawString(textString, positionX, positionY);
			}
			case RECTANGLE -> {
			g.setColor(color);
			g.fillRect(positionX, positionY, (int) size.getX(), (int) size.getY());
			}
		}

    }
}
