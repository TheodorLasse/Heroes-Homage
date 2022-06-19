package src.sprites.entities.livingEntities;

import src.Game;
import src.tools.Rotation;
import src.tools.time.DeltaTime;
import src.tools.time.DeltaTimer;

import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;

public class Animation {
    private LivingEntityState entityState;
    private LivingEntityState queuedState;
    private final Map<LivingEntityState, AnimationComponent> stateAnimations;
    private final Map<LivingEntityState, Double> timeBetweenFrames;
    private int currentFrame;
    private final DeltaTimer timer;
    private boolean forceAnimation = false;

    public Animation(Character.CharacterEnum character){
        this.entityState = LivingEntityState.IDLE;
        stateAnimations = new EnumMap<>(LivingEntityState.class);
        timeBetweenFrames = Map.ofEntries(
                Map.entry(LivingEntityState.IDLE, 0.3),
                Map.entry(LivingEntityState.ATTACK1, 0.13),
                Map.entry(LivingEntityState.ATTACK2, 0.13),
                Map.entry(LivingEntityState.DEATH, 0.2),
                Map.entry(LivingEntityState.DEAD, 1.0),
                Map.entry(LivingEntityState.RUN, 0.1),
                Map.entry(LivingEntityState.HIT, 0.2)
                );

        for (LivingEntityState state: LivingEntityState.values()){
            stateAnimations.put(state, Game.imageLoader.getCharacterAnimation(character, state));
        }
        timer = new DeltaTimer();
    }

    public void update(DeltaTime deltaTime){
        timer.update(deltaTime);
        final double animationTime = timeBetweenFrames.get(entityState) * stateAnimations.get(entityState).getAnimationLength();
        if (forceAnimation && timer.getElapsedSeconds() > animationTime) {
            forceAnimation = false;
            setAnimation(queuedState);
        }

        currentFrame = (int)(timer.getElapsedSeconds() / timeBetweenFrames.get(entityState));
    }

    public BufferedImage getAnimationFrame(Rotation rotation){
        double radiansPerIndex = Math.PI / 4;
        double rotationRadians = rotation.getRadians();
        double excessRadians = rotationRadians % radiansPerIndex;
        int rotationIndex = (int)((rotationRadians - excessRadians) / radiansPerIndex);
        return stateAnimations.get(entityState).getAnimationFrame(rotationIndex, currentFrame);
    }

    public LivingEntityState getEntityState() {
        return entityState;
    }

    /**
     * Sets the animation of a LivingEntity
     * @param state State to which the LivingEntity is set
     */
    public void setAnimation(LivingEntityState state){
        if (state == entityState || forceAnimation) return;
        entityState = state;
        timer.restart();
    }

    /**
     * Sets the animation of a LivingEntity and also forces the animation to not be interrupted until the end of the
     * animation cycle. Unless the interrupt is another forced animation
     * @param state State to which the LivingEntity is set and kept to.
     * @param nextState State which will be played after the forced animation has finished.
     */
    public void setAndForceAnimation(LivingEntityState state, LivingEntityState nextState){
        forceAnimation = false;
        setAnimation(state);
        forceAnimation = true;
        queuedState = nextState;
        timer.restart();
    }
}
