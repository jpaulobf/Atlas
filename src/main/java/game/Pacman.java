package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import engine.Game;

public class Pacman {

    private Game game;
    private Rectangle2D borders;
    private Rectangle2D nextBorders;
    private Ellipse2D circle;
    private double x = 0;
    private double y = 0;
    private int speed = 120; // pixels per second

    private final int UP = 0;
    private final int DOWN = 1;
    private final int LEFT = 2;
    private final int RIGHT = 3;
    private final int STOP = 4;

    private int currentDirection = STOP; // 0 = up, 1 = down, 2 = left, 3 = right
    private int nextDirection = STOP; // 0 = up, 1 = down, 2 = left, 3 = right
    private int oldNextDirection = STOP; // 0 = up, 1 = down, 2 = left, 3 = right

    /**
     * Pacman constructor
     * @param game
     * @param tileSize
     * @param offsetY
     */
    public Pacman(Game game, int tileSize, int offsetY) {
        this.game = game;
        this.x = (Game.CANVAS_WIDTH / 2) - (tileSize) + 1;
        this.y = offsetY + (tileSize * 22) + 1;

        this.borders = new Rectangle2D.Double(this.x,
                                              this.y,
                                              (tileSize * 2) - 2,
                                              (tileSize * 2) - 2);

        this.nextBorders = new Rectangle2D.Double(this.x,
                                                  this.y,
                                                  (tileSize * 2) - 2,
                                                  (tileSize * 2) - 2);

        this.circle = new Ellipse2D.Double(this.x + 4, this.y + 4, this.borders.getWidth() - 8, this.borders.getHeight() - 8);
    }

    public void update(double deltaTime) {
        
        //if (this.currentDirection != this.nextDirection) {
            
            if (nextDirection == UP) {
                double delta = (speed * (deltaTime / 1_000_000_000));
                this.nextBorders.setFrame(this.x, (this.y - delta), this.nextBorders.getWidth(), this.nextBorders.getHeight());

                if (!this.game.colliding(this.nextBorders)) {
                    this.y -= delta;
                    this.borders.setFrame(this.x, this.y, this.borders.getWidth(), this.borders.getHeight());
                    this.currentDirection = this.nextDirection;
                } else {
                    this.nextDirection = this.oldNextDirection;
                }
            } else if (nextDirection == DOWN) {
                double delta = (speed * (deltaTime / 1_000_000_000));
                this.nextBorders.setFrame(this.x, (this.y + delta), this.nextBorders.getWidth(), this.nextBorders.getHeight());

                if (!this.game.colliding(this.nextBorders)) {
                    this.y += delta;
                    this.borders.setFrame(this.x, this.y, this.borders.getWidth(), this.borders.getHeight());
                    this.currentDirection = this.nextDirection;
                } else {
                    this.nextDirection = this.oldNextDirection;
                }
            } else if (nextDirection == LEFT) {
                double delta = (speed * (deltaTime / 1_000_000_000));
                this.nextBorders.setFrame((this.x - delta), this.y, this.nextBorders.getWidth(), this.nextBorders.getHeight());

                if (!this.game.colliding(this.nextBorders)) {
                    this.x -= delta;
                    this.borders.setFrame(this.x, this.y, this.borders.getWidth(), this.borders.getHeight());
                    this.currentDirection = this.nextDirection;
                } else {
                    this.nextDirection = this.oldNextDirection;
                }
            } else if (nextDirection == RIGHT) {
                double delta = (speed * (deltaTime / 1_000_000_000));
                this.nextBorders.setFrame((this.x + delta), this.y, this.nextBorders.getWidth(), this.nextBorders.getHeight());

                if (!this.game.colliding(this.nextBorders)) { 
                    this.x += delta;
                    this.borders.setFrame(this.x, this.y, this.borders.getWidth(), this.borders.getHeight());
                    this.currentDirection = this.nextDirection;
                } else {
                    this.nextDirection = this.oldNextDirection;
                }
            }
            

        //}
        
        this.circle.setFrame(this.x + 4, this.y + 4, this.circle.getWidth(), this.circle.getHeight());
    }

    public void up(double deltaTime) {
        this.oldNextDirection = this.nextDirection;
        this.nextDirection = UP;
    } 
    
    public void down(double deltaTime) {
        this.oldNextDirection = this.nextDirection;
        this.nextDirection = DOWN;
    } 
    
    public void left(double deltaTime) {
        this.oldNextDirection = this.nextDirection;
        this.nextDirection = LEFT;
    }

    public void right(double deltaTime) {
        this.oldNextDirection = this.nextDirection;
        this.nextDirection = RIGHT;
    }

    public Rectangle2D getBorders() {
        return borders;
    }

    public void render(Graphics2D g) {
        g.setColor(Color.YELLOW);
        g.fill(this.circle);
    }
}
