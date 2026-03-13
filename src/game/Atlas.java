package game;

import engine.Game;
import java.awt.Color;
import java.awt.Graphics;

/**
 * The Atlas game.
 */
public class Atlas extends Game {

    public Atlas() {
        super("Atlas Game", 1280, 720);
    }

    @Override
    public void init() {
    }

    @Override
    public void onUpdate(long deltaTime) {
    }

    @Override
    public void onRender(Graphics g, double interpolation) {
        g.setColor(Color.RED);
        g.fillRect(100, 100, 200, 200);
    }

    public static void main(String[] args) {
        new Atlas().start();
    }
}
