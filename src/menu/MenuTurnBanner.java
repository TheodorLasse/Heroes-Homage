package src.menu;

import src.Game;

import javax.swing.*;
import java.awt.*;

public class MenuTurnBanner extends JLabel {
    final private Game game;

    public MenuTurnBanner(Game game){
        this.game = game;
    }

    private void paintBanner(Graphics g){
        g.setColor(game.getCurrentPlayer().getColor());
        Dimension labelSize = this.getSize();
        int ovalWidth = labelSize.width / 2;
        int ovalHeight = ovalWidth / 2;
        int ovalX = ovalWidth / 2;
        int ovalY = ovalHeight / 2;

        g.fillOval(ovalX, ovalY, ovalWidth, ovalHeight);

        String team = game.getCurrentPlayer().getTeamColor().toString();
        int stringX = ovalX + (ovalWidth - g.getFont().getSize() * team.length()) / 2;
        int stringY = ovalY + ovalHeight / 2;
        g.setColor(Color.BLACK);
        g.drawString(team,stringX , stringY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintBanner(g);
    }
}
