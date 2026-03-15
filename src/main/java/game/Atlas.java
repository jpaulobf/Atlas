package game;

import engine.Game;

import java.awt.Graphics2D;
import audio.Audio;
import java.util.Random;

/**
 * The Atlas game.
 */
public class Atlas extends Game {

    protected Background background;


    public Atlas() {
        super("Pacman", 1600, 900, 60);
    }

    @Override
    public void init() {
        background = new Background();
    }

    @Override
    public void onUpdate(long deltaTime) {
        
    }

    @Override
    public void onRender(Graphics2D g, double interpolation) {
        g.drawImage(background.getImage(), 0, 0, null);

    }

    public static void main(String[] args) {
        new Atlas().start();
    }

    @Override
    public void toggleColisionBorders() {
        this.background.toggleColisionBorders();
    }
}
