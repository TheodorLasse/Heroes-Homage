package src.sprites.Entities;

import src.tools.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class CombatEntity extends Entity {
    public CombatEntity(Vector2D position, Vector2D size, BufferedImage texture) {
        super(position, size, 0, texture);
    }

    @Override
    public void draw(Graphics g, JComponent jc) {

    }
}
