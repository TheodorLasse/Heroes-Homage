package src.sprites.Entities.LivingEntities;

import src.Game;
import src.sprites.Entities.Entity;
import src.sprites.Entities.EntityHandler;
import src.sprites.Entities.EntityType;
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

public abstract class LivingEntity extends Entity {
    protected Animation animation;
    protected PlayerTeam team;
    protected EntityHandler entityHandler;
    protected BufferedImage flag;
    protected Path path;
    protected double timeUntilMove = 0;
    protected double timeBetweenMoves = 0.2;

    /**
     * A unit on the GameMap that can move, belongs to a team and has an army
     * @param position Entity's position
     * @param character Entity's character, i.e list of animations
     * @param team Entity's team
     * @param entityHandler EntityHandler which keeps track of Entities on the GameMap
     */
    public LivingEntity(Vector2D position, ImageLoader.Character character, PlayerTeam team, EntityHandler entityHandler) {
        super(position, new Vector2D(1, 1), 0, null);
        this.entityHandler = entityHandler;
        this.team = team;
        this.animation = new Animation(character);
        this.texture = animation.getAnimationFrame();
        setEntityType(EntityType.LIVING);

        switch (team.getTeamColor()){
            case RED -> flag = Game.imageLoader.getImage(ImageLoader.ImageName.RED_FLAG);
            case BLUE -> flag = Game.imageLoader.getImage(ImageLoader.ImageName.BLUE_FLAG);
            default -> flag = Game.imageLoader.getImage(ImageLoader.ImageName.ERROR);
        }
    }

    @Override
    public void update(DeltaTime deltaTime, WindowFocus focus) {
        super.update(deltaTime, focus);
        move(deltaTime);
        animation.update(deltaTime);
        this.texture = animation.getAnimationFrame();
    }

    protected void move(DeltaTime deltaTime){
        if (path == null || path.getLength() == 0) return;
        //animation.setAnimation(LivingEntityState.RUN);

        timeUntilMove -= deltaTime.getSeconds();
        if (timeUntilMove <= 0){
            Path.Step nextStep = path.popStep();
            this.position.setX(nextStep.getX());
            this.position.setY(nextStep.getY());
            timeUntilMove = timeBetweenMoves;
        }
        //if (path.getLength() == 0) animation.setAnimation(LivingEntityState.IDLE);
    }

    protected void updateTexture(){}

    protected void interact(Vector2D position){
        for (Entity entity : entityHandler.getIterator()){
            if (entity.isOverlap(position) && entity != this){
                interactAction(entity);
            }
        }
    }

    protected void interactAction(Entity entity){
    }

    @Override
    public void onMouseClick3(PathMap map, PathFinder finder, Vector2D mouseMapPos) {
        Vector2D mouseRounded = new Vector2D((int)mouseMapPos.getX(), (int)mouseMapPos.getY());
        if (mouseRounded.getX() < 0 || mouseRounded.getY() < 0) return;

        Vector2D diff = Vector2D.getDifference(position, mouseRounded);
        if (diff.getLength() <= 1.42){
            interact(mouseMapPos);
        }
        finder.setMap(map);
        path = finder.findPath(this, (int)position.getX(), (int)position.getY(),
                (int)mouseRounded.getX(), (int)mouseRounded.getY());
    }

    @Override
    public void draw(Graphics g, JComponent gc) {
        if(flag != null){
            g.drawImage(flag, (int) relativePosition.getX(), (int) relativePosition.getY(), gc);
        }
        super.draw(g, gc);
    }

    public PlayerTeam getPlayerTeam(){
        return team;
    }
}
