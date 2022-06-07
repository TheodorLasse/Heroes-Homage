package src.sprites.Entities;

import src.Army;
import src.Game;
import src.sprites.Entities.MapEntities.MapEntity;
import src.tools.WindowFocus;
import src.player.PlayerTeam;
import src.tools.aStar.PathFinder;
import src.tools.aStar.PathMap;
import src.tools.image.ImageLoader;
import src.tools.Vector2D;
import src.tools.aStar.Path;
import src.tools.time.DeltaTime;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class LivingEntity extends MapEntity {
    private final Game game;
    private Army army;
    private final EntityHandler mapEntityHandler;
    private BufferedImage flag;
    private Path path;
    double timeUntilMove = 0;
    double timeBetweenMoves = 0.2;

    /**
     * A unit on the GameMap that can move, belongs to a team and has an army
     * @param position Entity's position
     * @param texture Entity's texture, i.e BufferedImage
     * @param game The game object which this Entity calls on for starting combat
     * @param team Entity's team
     * @param mapEntityHandler EntityHandler which keeps track of Entities on the GameMap
     */
    public LivingEntity(Vector2D position, BufferedImage texture, Game game, PlayerTeam team, EntityHandler mapEntityHandler) {
        super(position, texture, team);
        this.game = game;
        this.mapEntityHandler = mapEntityHandler;
        this.army = new Army(team);
        setEntityType(EntityType.LIVING);

        switch (playerTeam.getTeamColor()){
            case RED -> flag = Game.imageLoader.getImage(ImageLoader.ImageName.RED_FLAG);
            case BLUE -> flag = Game.imageLoader.getImage(ImageLoader.ImageName.BLUE_FLAG);
            default -> flag = Game.imageLoader.getImage(ImageLoader.ImageName.ERROR);
        }
    }

    @Override
    public void update(DeltaTime deltaTime, WindowFocus focus) {
        super.update(deltaTime, focus);
        move(deltaTime);
    }

    private void move(DeltaTime deltaTime){
        if (path == null || path.getLength() == 0) return;

        timeUntilMove -= deltaTime.getSeconds();
        if (timeUntilMove <= 0){
            Path.Step nextStep = path.popStep();
            this.position.setX(nextStep.getX());
            this.position.setY(nextStep.getY());
            timeUntilMove = timeBetweenMoves;
        }
    }

    private void interact(Vector2D position){
        for (Entity entity : mapEntityHandler.getIterator()){
            if (entity.isOverlap(position) && entity != this){
                switch (entity.getEntityType()){
                    case COLLECTABLE -> {

                    }
                    case LIVING -> {
                        LivingEntity otherEntity = (LivingEntity) entity;
                        if (otherEntity.getPlayerTeam() != this.playerTeam) {
                            game.newCombat(this.army, otherEntity.getArmy());
                        }
                    }
                    case NONE -> {
                    }
                }
            }
        }

    }

    public Army getArmy() {
        return army;
    }

    @Override
    public void onMouseClick3(PathMap map, PathFinder finder, Vector2D mouseMapPos) {
        Vector2D mouseRounded = new Vector2D((int)mouseMapPos.getX(), (int)mouseMapPos.getY());
        Vector2D diff = Vector2D.getDifference(position, mouseRounded);
        if (diff.getLength() <= 1.42){
            interact(mouseMapPos);
        }
        finder.setMap(map);
        path = finder.findPath(this, (int)position.getX(), (int)position.getY(),
                (int)mouseMapPos.getX(), (int)mouseMapPos.getY());
    }

    @Override
    public void draw(Graphics g, JComponent gc) {
        if(flag != null){
            g.drawImage(flag, (int) relativePosition.getX(), (int) relativePosition.getY(), gc);
        }
        super.draw(g, gc);
    }
}
