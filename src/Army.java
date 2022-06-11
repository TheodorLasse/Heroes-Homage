package src;

import src.player.PlayerTeam;
import src.sprites.entities.livingEntities.Character;
import src.sprites.entities.livingEntities.CombatLivingEntity;
import src.sprites.entities.EntityHandler;
import src.tools.Vector2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Army {
    private final PlayerTeam team;
    private final ArrayList<CombatLivingEntity> combatEntities;


    public Army(PlayerTeam team){
        this.team = team;
        combatEntities = new ArrayList<>();

        combatEntities.add(new CombatLivingEntity(Character.CharacterEnum.NINJA_1, team));
        combatEntities.add(new CombatLivingEntity(Character.CharacterEnum.NINJA_2, team));
        combatEntities.add(new CombatLivingEntity(Character.CharacterEnum.NINJA_3, team));
        combatEntities.add(new CombatLivingEntity(Character.CharacterEnum.NINJA_4, team));
        combatEntities.add(new CombatLivingEntity(Character.CharacterEnum.NINJA_5, team));
        combatEntities.add(new CombatLivingEntity(Character.CharacterEnum.NINJA_6, team));
    }

    public ArrayList<CombatLivingEntity> getCombatEntities() {
        return combatEntities;
    }
}
