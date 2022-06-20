package src.sprites.entities.livingEntities;

import src.tools.Rotation;

import java.awt.image.BufferedImage;
import java.util.List;

public class AnimationComponent {
    private final List<List<BufferedImage>> animationFrames;

    public AnimationComponent(List<List<BufferedImage>> animationFrames){
        this.animationFrames = animationFrames;
    }

    /**
     * Returns the image of a certain instance in an animation
     * @param rotation Which direction the character is facing
     * @param frame Which stage of the animation the character is currently in
     * @return The image of a certain rotation in a certain stage of the animation.
     */
    public BufferedImage getAnimationFrame(Rotation rotation, int frame) {
        double radiansPerIndex = Math.PI * 2 / animationFrames.size();
        double rotationRadians = rotation.getRadians();
        double excessRadians = rotationRadians % radiansPerIndex;
        int rotationIndex = (int)((rotationRadians - excessRadians) / radiansPerIndex);

        List<BufferedImage> rotatedAnimation = animationFrames.get(rotationIndex);
        int translatedFrame = frame % rotatedAnimation.size();
        return rotatedAnimation.get(translatedFrame);
    }

    public int getAnimationLength(){
        return animationFrames.get(0).size();
    }
}
