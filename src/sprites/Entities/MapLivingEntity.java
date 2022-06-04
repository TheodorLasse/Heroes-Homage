package src.sprites.Entities;

import src.Game;
import src.GameMap;
import src.tools.ImageLoader;
import src.tools.MapFocus;
import src.tools.Vector2D;
import src.tools.aStar.Path;
import src.tools.time.DeltaTime;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MapLivingEntity extends MapEntity {
    private TeamType team = TeamType.NEUTRAL;
    private BufferedImage flag;
    Path path;
    double timeUntilMove = 0;
    double timeBetweenMoves = 0.1;

    public MapLivingEntity(Vector2D position, Vector2D size, double rotation, BufferedImage texture, TeamType team) {
        super(position, size, rotation, texture);
        init(team);
    }

    public MapLivingEntity(Vector2D position, BufferedImage texture, TeamType team) {
        super(position, new Vector2D(1, 1), 0, texture);
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
    public void update(DeltaTime deltaTime, MapFocus mapFocus) {
        super.update(deltaTime, mapFocus);
        move(deltaTime);
    }

    private void move(DeltaTime deltaTime){

        if (path == null || path.getLength() == 0) return;

        timeUntilMove -= deltaTime.getSeconds();
        if (timeUntilMove <= 0){
            Path.Step nextStep = path.popStep();
            position.setX(nextStep.getX());
            position.setY(nextStep.getY());
            timeUntilMove = timeBetweenMoves;
        }
    }

    @Override
    public void onMouseClick3(GameMap map, Vector2D mousePos) {
        super.onMouseClick3(map, mousePos);
        Vector2D mapPos = map.relativeToAbsolutePos(mousePos);
        path = map.getPath(this, mapPos);
    }

    @Override
    public void draw(Graphics g, JComponent gc) {
        if(flag != null){
            g.drawImage(flag, (int) relativePosition.getX() * GameMap.TILE_SIZE, (int) relativePosition.getY() * GameMap.TILE_SIZE, gc);
        }
        super.draw(g, gc);
    }
}
