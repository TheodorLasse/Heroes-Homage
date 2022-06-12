package src.sprites.entities.livingEntities;

import src.tools.JsonReader;

import java.util.Map;
import java.util.Random;

public class CombatStats {
    private int stackSize;
    private int totalHealth;
    private final int maxHealth;
    private final int minDamage;
    private final int maxDamage;
    private final int initiative;
    private final Random rnd = new Random();

    /**
     * The combat stats of a CombatLivingEntity
     * @param stackSize Size of the stack of CombatLivingEntities
     * @param character The character of the CombatLivingEntity
     */
    public CombatStats(int stackSize, Character.CharacterEnum character){
        this.stackSize = stackSize;
        Map<?, ?> jsonMap = JsonReader.readJsonCritical(character);
        maxHealth = (int) (double) jsonMap.get("maxHealth");
        minDamage = (int) (double) jsonMap.get("minDamage");
        maxDamage = (int) (double) jsonMap.get("maxDamage");
        initiative = (int) (double) jsonMap.get("initiative");
        totalHealth = maxHealth * stackSize;
    }

    /**
     * Rolls damage for this set of stats.
     * @return Damage
     */
    public int rollDamage(){
        if (stackSize <= 0) return 0;
        return rnd.nextInt((maxDamage - minDamage) * stackSize) + minDamage * stackSize;
    }

    /**
     * Inflict damage on this set of stats.
     * @param damage Damage that was taken.
     */
    public void takeDamage(int damage){
        totalHealth -= damage;
        stackSize = (int)Math.ceil((double)totalHealth / maxHealth);
    }

    public int getTotalHealth() {
        return totalHealth;
    }

    public int getStackSize() {
        return stackSize;
    }

    public int getInitiative(){
        return initiative;
    }
}
