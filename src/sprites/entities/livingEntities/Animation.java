package src.sprites.entities.livingEntities;

import src.Game;
import src.tools.time.DeltaTime;
import src.tools.time.DeltaTimer;

import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Animation {
    private LivingEntityState entityState;
    private LivingEntityState queuedState;
    private final Map<LivingEntityState, List<BufferedImage>> stateAnimations;
    private int currentFrame;
    private final DeltaTimer timer;
    private boolean forceAnimation = false;

    /**
     * From enum to state length
     */
    final private static Map<LivingEntityState, Integer> animationStateLength = Map.ofEntries(
            Map.entry(LivingEntityState.IDLE, 4), Map.entry(LivingEntityState.RUN, 8),
            Map.entry(LivingEntityState.JUMP, 2), Map.entry(LivingEntityState.FALL, 2),
            Map.entry(LivingEntityState.ATTACK1, 4), Map.entry(LivingEntityState.ATTACK2, 4),
            Map.entry(LivingEntityState.HIT, 4), Map.entry(LivingEntityState.DEATH, 3),
            Map.entry(LivingEntityState.DEAD, 1)
    );

    public Animation(Character.CharacterEnum character, LivingEntityState entityState){
        this.entityState = entityState;
        stateAnimations = new EnumMap<>(LivingEntityState.class);
        List<BufferedImage> allFrames = Game.imageLoader.getCharacter(character);

        int i = 0;
        for (LivingEntityState state: LivingEntityState.values()){
            stateAnimations.put(state, allFrames.subList(i, i + animationStateLength.get(state)));
            i += animationStateLength.get(state);
        }
        timer = new DeltaTimer();
    }

    public void update(DeltaTime deltaTime){
        timer.update(deltaTime);
        final double timeBetweenFrames = 0.3;
        List<BufferedImage> currentList = stateAnimations.get(entityState);
        if (timer.getElapsedSeconds() > timeBetweenFrames * currentList.size()) {
            if (forceAnimation) {
                forceAnimation = false;
                setAnimation(queuedState);
            }
            timer.restart();
        }
        currentFrame = (int)(timer.getElapsedSeconds()/ timeBetweenFrames);
    }

    public BufferedImage getAnimationFrame(){
        return stateAnimations.get(entityState).get(currentFrame);
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
    }
}
