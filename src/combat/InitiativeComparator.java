package src.combat;

import src.sprites.entities.livingEntities.CombatLivingEntity;

import java.util.Comparator;

public class InitiativeComparator implements Comparator<CombatLivingEntity> {
    @Override
    public int compare(CombatLivingEntity o1, CombatLivingEntity o2) {
        return Integer.compare(o1.getInitiative(), o2.getInitiative());
    }
}
