package src.map;

import src.player.PlayerTeam;

import java.util.ArrayList;

public class MapTurn {
    private final ArrayList<PlayerTeam> playerTeamList;
    private int playerTurnIndex = 0;

    public MapTurn(ArrayList<PlayerTeam> playerTeamList){
        this.playerTeamList = playerTeamList;
    }

    public void nextPlayersTurn(){
        playerTurnIndex++;
        if (playerTurnIndex >= playerTeamList.size()) playerTurnIndex = 0;
    }

    public PlayerTeam getCurrentPlayer() {
        return playerTeamList.get(playerTurnIndex);
    }
}
