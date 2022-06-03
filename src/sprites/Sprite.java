package src.sprites;
import src.data.GameComponent;
import src.data.Vector2D;

import java.awt.*;

/**
 * The most basic type that can be rendered inside the game.
 */
public interface Sprite
{
    Vector2D getPosition();
    Vector2D getSize();
    double getRotation();

    void draw(final Graphics g, final GameComponent gc);
}
