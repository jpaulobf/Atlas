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
    protected Pacman pacman;
    private final int tileSize = 24;
    private final int offsetX = (Game.CANVAS_WIDTH / 2) - (tileSize * 34 / 2);
    private final int offsetY = 20;

    public Atlas() {
        super("Pacman", 1600, 900, 0);
        this.pacman = new Pacman(tileSize, offsetY);
    }

    @Override
    public void init() {
        background = new Background(tileSize, tileSize, offsetX, offsetY);
    }

    @Override
    public void onUpdate(long deltaTime) {
        System.out.println(this.background.colliding(pacman.getBorders()));
        
    }

    @Override
    public void onRender(Graphics2D g, double interpolation) {
        background.render(g);

        pacman.render(g);
    }

    @Override
    public void toggleCollisionBorders() {
        this.background.toggleCollisionBorders();
    }

    public static void main(String[] args) {
        new Atlas().start();
    }
}
