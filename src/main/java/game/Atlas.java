package game;

import engine.Game;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import audio.Audio;
import java.util.Random;

/**
 * The Atlas game.
 */
public class Atlas extends Game {

    Background background;
    Pacman pacman;
    private final int tileSize = 24;
    private final int offsetX = (Game.CANVAS_WIDTH / 2) - (tileSize * 34 / 2);
    private final int offsetY = 20;

    public Atlas() {
        super("Pacman", 1600, 900, 0);
        this.pacman = new Pacman(this, tileSize, offsetY);
    }

    @Override
    public void init() {
        background = new Background(tileSize, tileSize, offsetX, offsetY);
    }

    @Override
    public void onUpdate(long deltaTime) {
        if (this.input.isKeyDown(java.awt.event.KeyEvent.VK_UP)) {
            pacman.up(deltaTime);
        } else if (this.input.isKeyDown(java.awt.event.KeyEvent.VK_DOWN)) {
            pacman.down(deltaTime);
        } 
        
        if (this.input.isKeyDown(java.awt.event.KeyEvent.VK_LEFT)) {
            pacman.left(deltaTime);
        } else if (this.input.isKeyDown(java.awt.event.KeyEvent.VK_RIGHT)) {
            pacman.right(deltaTime);
        }

        pacman.update(deltaTime);
        
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

    @Override
    public boolean colliding(Rectangle2D borders) {
        return this.background.colliding(borders);
    }
}
