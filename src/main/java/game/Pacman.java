package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import engine.Game;

public class Pacman {

    protected Rectangle2D borders;

    public Pacman(int tileSize, int offsetY) {
        this.borders = new Rectangle2D.Double((Game.CANVAS_WIDTH / 2) - (tileSize) + 1,
                                               offsetY + (tileSize * 22) + 1,
                                              (tileSize * 2) - 2,
                                              (tileSize * 2) - 2);

    }

    public Rectangle2D getBorders() {
        return borders;
    }

    public void render(Graphics2D g) {
        g.setColor(Color.YELLOW);
        g.fill(borders);
    }
}
