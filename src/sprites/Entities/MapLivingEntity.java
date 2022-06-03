package src.sprites.Entities;

import src.Game;
import src.data.GameComponent;
import src.data.GameMap;
import src.data.ImageLoader;
import src.data.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MapLivingEntity extends MapEntity {
    TeamType team = TeamType.NEUTRAL;
    BufferedImage flag;

    public MapLivingEntity(Vector2D position, Vector2D size, double rotation, BufferedImage texture, TeamType team) {
        super(position, size, rotation, texture);
        init(team);
    }

    public MapLivingEntity(Vector2D position, BufferedImage texture, TeamType team) {
        super(position, new Vector2D(), 0, texture);
        init(team);
    }

    private void init(TeamType team){
        this.team = team;
        switch (team){
            case RED -> flag = Game.imageLoader.getImage(ImageLoader.ImageName.RED_FLAG);
            case BLUE -> flag = Game.imageLoader.getImage(ImageLoader.ImageName.BLUE_FLAG);
        }
    }

    @Override
    public void draw(Graphics g, GameComponent gc) {
        if(flag != null){
            g.drawImage(flag, (int) relativePosition.getX() * GameMap.TILE_SIZE, (int) relativePosition.getY() * GameMap.TILE_SIZE, gc);
        }
        super.draw(g, gc);
    }
}
