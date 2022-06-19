package src.sprites.entities.livingEntities;

import java.awt.image.BufferedImage;
import java.util.List;

public class AnimationComponent {
    private final List<List<BufferedImage>> animationFrames;

    public AnimationComponent(List<List<BufferedImage>> animationFrames){
        this.animationFrames = animationFrames;
    }

    public BufferedImage getAnimationFrame(int rotation, int frame) {
        List<BufferedImage> rotatedAnimation = animationFrames.get(rotation);
        int translatedFrame = frame % rotatedAnimation.size();
        return rotatedAnimation.get(translatedFrame);
    }

    public int getAnimationLength(){
        return animationFrames.get(0).size();
    }
}
