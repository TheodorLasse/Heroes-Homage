package src.sprites.entities.livingEntities;

import src.Game;
import src.player.PlayerTeam;
import src.sprites.Sprite;
import src.sprites.SpriteHandler;
import src.sprites.SpriteLayer;
import src.sprites.SpriteTexture;
import src.sprites.entities.Entity;
import src.sprites.entities.EntityHandler;
import src.sprites.entities.EntityType;
import src.tools.Vector2D;
import src.tools.WindowFocus;
import src.tools.image.ImageLoader;
import src.tools.time.DeltaTime;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class CombatLivingEntity extends LivingEntity {
    protected CombatStats stats;
    protected SpriteHandler spriteHandler;
    protected boolean isEntityTurn = false;
    public CombatLivingEntity(Character.CharacterEnum character, PlayerTeam team) {
        super(new Vector2D(), character, team, null);
        timeBetweenMoves = 0.5;
        stats = new CombatStats(100, character);
        maxMovement = stats.getMaxMovement();
        spriteHandler = new SpriteHandler();
    }

    @Override
    public void update(DeltaTime deltaTime, WindowFocus focus) {
        super.update(deltaTime, focus);
        spriteHandler.update(deltaTime);
    }

    @Override
    protected void interactAction(Entity entity) {
        super.interactAction(entity);
        if (entity.getEntityType() == EntityType.LIVING) {
            attack((CombatLivingEntity) entity);
        }
    }

    @Override
    protected boolean interactConditions(Entity entity, Vector2D InteractPosition) {
        return super.interactConditions(entity, InteractPosition) &&
                ((LivingEntity)entity).getPlayerTeam() != this.team && ((LivingEntity)entity).alive;
    }

    private void attack(CombatLivingEntity entity) {
        if (entity.isDead() || entity.getPlayerTeam() == this.team) return;
        int damage = stats.rollDamage();
        entity.underAttack(damage);

        animation.setAndForceAnimation(LivingEntityState.ATTACK1, LivingEntityState.IDLE);
    }

    public void underAttack(int damage){
        int amountDead = stats.takeDamage(damage);
        if (stats.getTotalHealth() <= 0){
            alive = false;
            animation.setAndForceAnimation(LivingEntityState.DEATH, LivingEntityState.DEAD);
        } else animation.setAndForceAnimation(LivingEntityState.HIT, LivingEntityState.IDLE);
        createHitSplat(amountDead);
    }

    @Override
    protected Vector2D getRelativeMapPosition(Vector2D pos, Vector2D focusPos){
        return Vector2D.getSum(focusPos, position);
    }

    public int getInitiative(){
        return stats.getInitiative();
    }

    public void setCombatEntityHandler(EntityHandler combatEntityHandler){
        this.entityHandler = combatEntityHandler;
    }

    public void setSize(Vector2D size){
        this.size = size;
    }

    public void setEntityTurn(boolean isEntityTurn){
        this.isEntityTurn = isEntityTurn;
        resetMovement();
    }

    public boolean isDead() {
        return !alive;
    }

    /**
     * creates and adds a SpriteTexture depicting a hit splat
     * @param amountDead amount dead to draw
     */
    protected void createHitSplat(int amountDead) {
        BufferedImage skull = Game.imageLoader.getImage(ImageLoader.ImageName.SKULL);
        BufferedImage splatImage = new BufferedImage(skull.getWidth() + 20, skull.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = splatImage.getGraphics();

        g.drawImage(skull, splatImage.getWidth() - skull.getWidth(), 0, null);
        g.setColor(Color.WHITE);
        g.drawString(Integer.toString(amountDead), 0, splatImage.getHeight());

        SpriteTexture hitSplat = new SpriteTexture(drawPosition, 0, splatImage);
        spriteHandler.add(hitSplat, 5, SpriteLayer.LAST);
    }
    @Override
    protected void drawBanner(Graphics g, JComponent gc) {
        if (!alive) return; // Don't draw the banners of dead guys

        Graphics2D g2 = (Graphics2D) g;
        final int bannerWidth = 26;
        final int bannerHeight = bannerWidth / 2;

        final int bannerOffsetY = - (int)(tileSize * 0.5);
        final int bannerOffsetX = (int)((size.getX() * tileSize  - bannerWidth) * 0.5);
        final int drawX = (int)(drawPosition.getX() + bannerOffsetX);
        final int drawY = (int)(drawPosition.getY() + bannerOffsetY);

        //Draw inside of rect
        g2.setColor(Color.GRAY);
        g2.fillRect(drawX, drawY, bannerWidth, bannerHeight);
        //Fill rect
        g2.setColor(team.getColor());
        Stroke oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(drawX, drawY, bannerWidth, bannerHeight);
        g2.setStroke(oldStroke);
        //Draw stack size
        g2.setColor(Color.BLACK);
        g2.drawString(Integer.toString(stats.getStackSize()), drawX + bannerWidth/3, drawY + g.getFont().getSize());
    }

    @Override
    public void draw(Graphics g, JComponent gc) {
        super.draw(g, gc);
        for (Sprite iterSprite : spriteHandler.getLayerIterator(SpriteLayer.LAST)){
            iterSprite.draw(g, gc);
        }
    }
}
