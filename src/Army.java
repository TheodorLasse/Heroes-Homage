package src;

import src.player.PlayerTeam;
import src.sprites.entities.livingEntities.Character;
import src.sprites.entities.livingEntities.CombatLivingEntity;

import java.util.ArrayList;

public class Army {
    private final PlayerTeam team;
    private final ArrayList<CombatLivingEntity> combatEntities;


    public Army(PlayerTeam team){
        this.team = team;
        combatEntities = new ArrayList<>();

        combatEntities.add(new CombatLivingEntity(Character.CharacterEnum.ORC, team));
    }

    public ArrayList<CombatLivingEntity> getCombatEntities() {
        return combatEntities;
    }
}
