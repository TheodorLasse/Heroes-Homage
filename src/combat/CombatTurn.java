package src.combat;

import src.sprites.entities.livingEntities.CombatLivingEntity;

import java.util.ArrayList;

public class CombatTurn {
    private final ArrayList<CombatLivingEntity> entities;
    private int entityIndex;

    /**
     * Keeps track of which CombatLivingEntity's turn it currently is on the battlefield
     * @param entities The CombatLivingEntities that are on the battlefield.
     */
    public CombatTurn(ArrayList<CombatLivingEntity> entities) {
        this.entities = entities;
        this.entities.sort(new InitiativeComparator());
        entityIndex = 0;
        entities.get(entityIndex).setEntityTurn(true);
    }

    public CombatLivingEntity getCurrentEntityTurn(){
        return entities.get(entityIndex);
    }

    /**
     * Ends the current CombatLivingEntity's turn
     */
    public void endEntityTurn(){
        entities.get(entityIndex).setEntityTurn(false);
        do {
        entityIndex++;
        if (entityIndex >= entities.size()) entityIndex = 0;
        }while(entities.get(entityIndex).isDead());
        entities.get(entityIndex).setEntityTurn(true);
    }
}
