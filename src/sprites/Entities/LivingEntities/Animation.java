package src.sprites.Entities.LivingEntities;

import src.Game;
import src.tools.image.ImageLoader;
import src.tools.time.DeltaTime;
import src.tools.time.DeltaTimer;

import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Animation {
    private LivingEntityState entityState;
    private final Map<LivingEntityState, List<BufferedImage>> stateAnimations;
    private int currentFrame;
    private final DeltaTimer timer;

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

    public Animation(ImageLoader.Character character, LivingEntityState entityState){
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
        if (timer.getElapsedSeconds() > timeBetweenFrames * currentList.size()) timer.restart();
        currentFrame = (int)(timer.getElapsedSeconds()/ timeBetweenFrames);
    }

    public BufferedImage getAnimationFrame(){
        return stateAnimations.get(entityState).get(currentFrame);
    }

    public void setAnimation(LivingEntityState state){
        if (state == entityState) return;
        timer.restart();
        entityState = state;
    }
}
