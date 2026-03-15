package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import engine.Game;
import util.NewRoundRectangle2D;

/**
 * The Background class represents the static background of the game, including the
 * layout of walls and paths. It generates a BufferedImage based on a predefined
 * scenario, which is a 2D array of bytes where each value corresponds to a specific
 * type of tile (e.g., wall, path, pellet). The class also manages collision borders
 * for the walls, which can be toggled on and off for debugging purposes. The background
 * is rendered onto the game canvas, and the collision borders are drawn as red rectangles
 * when enabled. The class provides methods to retrieve the background image and to toggle
 * the visibility of collision borders.
 */
public class Background {

    // Cache for the background image
    private BufferedImage image;
    private int cellWidth = 24;
    private int cellHeight = 24;
    private boolean showCollisionBorders = false;
    private int offsetX = 0;
    private int offsetY = 0;

    private final Rectangle2D[] collisions;

    // Scenario data
    private byte[][] scenario = {
            { 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3 },
            { 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1 },
            { 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1 },
            { 1, 0, 0, 5, 0, 0, 6, 0, 0, 9, 1, 1, 1, 1, 1, 8, 0, 0, 9, 1, 1, 1, 1, 1, 8, 0, 0, 6, 0, 0, 5, 0, 0, 1 },
            { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
            { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
            { 1, 1, 1, 8, 0, 0, 7, 0, 0, 7, 0, 0, 1, 0, 0, 2, 1, 1, 3, 0, 0, 1, 0, 0, 7, 0, 0, 7, 0, 0, 9, 1, 1, 1 },
            { 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1 },
            { 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1 },
            { 1, 0, 0, 5, 0, 0, 1, 0, 0, 1, 0, 0, 6, 0, 0, 4, 1, 1, 11, 0, 0, 6, 0, 0, 1, 0, 0, 1, 0, 0, 5, 0, 0, 1 },
            { 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1 },
            { 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1 },
            { 1, 1, 1, 1, 1, 1, 11, 0, 0, 1, 0, 0, 9, 1, 1, 1, 1, 1, 1, 1, 1, 8, 0, 0, 1, 0, 0, 4, 1, 1, 1, 1, 1, 1 },
            { 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
            { 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
            { 6, 0, 0, 9, 1, 1, 1, 1, 1, 11, 0, 0, 2, 1, 1, 8, 0, 0, 9, 1, 1, 3, 0, 0, 4, 1, 1, 1, 1, 1, 8, 0, 0, 6 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 7, 0, 0, 9, 1, 1, 1, 1, 1, 3, 0, 0, 4, 1, 1, 1, 1, 1, 1, 1, 1, 11, 0, 0, 2, 1, 1, 1, 1, 1, 8, 0, 0, 7 },
            { 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
            { 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
            { 1, 1, 1, 1, 1, 1, 3, 0, 0, 1, 0, 0, 9, 1, 1, 1, 1, 1, 1, 1, 1, 8, 0, 0, 1, 0, 0, 2, 1, 1, 1, 1, 1, 1 },
            { 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1 },
            { 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1 },
            { 1, 0, 0, 5, 0, 0, 1, 0, 0, 1, 0, 0, 7, 0, 0, 2, 1, 1, 3, 0, 0, 7, 0, 0, 1, 0, 0, 1, 0, 0, 5, 0, 0, 1 },
            { 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1 },
            { 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1 },
            { 1, 1, 1, 8, 0, 0, 6, 0, 0, 6, 0, 0, 1, 0, 0, 4, 1, 1, 11, 0, 0, 1, 0, 0, 6, 0, 0, 6, 0, 0, 9, 1, 1, 1 },
            { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
            { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
            { 1, 0, 0, 5, 0, 0, 7, 0, 0, 9, 1, 1, 1, 1, 1, 8, 0, 0, 9, 1, 1, 1, 1, 1, 8, 0, 0, 7, 0, 0, 5, 0, 0, 1 },
            { 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1 },
            { 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1 },
            { 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 11 }
    };

    /**
     * Constructs a new Background instance with the specified cell dimensions and
     * offsets. The
     * cellWidth and cellHeight parameters determine the size of each tile in the
     * background, while
     * the offsetX and offsetY parameters specify the position of the background
     * within the game canvas. The background image is generated based on the
     * scenario data, which defines the layout of walls and paths in the game.
     * 
     * @param cellWidth
     * @param cellHeight
     * @param offsetX
     * @param offsetY
     */
    public Background(int cellWidth, int cellHeight, int offsetX, int offsetY) {
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        
        this.collisions = new Rectangle2D[] {
            new Rectangle2D.Double(this.offsetX, this.offsetY, 34 * cellWidth, cellHeight),
            new Rectangle2D.Double(this.offsetX, this.offsetY + (1 * cellHeight), cellWidth, 15 * cellHeight),
            new Rectangle2D.Double(this.offsetX, this.offsetY + (18 * cellHeight), cellWidth, 15 * cellHeight),
            new Rectangle2D.Double(this.offsetX, this.offsetY + (33 * cellHeight), 34 * cellWidth, cellHeight),
            new Rectangle2D.Double(this.offsetX + (33 * cellWidth), this.offsetY + (1 * cellHeight), cellWidth, 15 * cellHeight),
            new Rectangle2D.Double(this.offsetX + (33 * cellWidth), this.offsetY + (18 * cellHeight), cellWidth, 15 * cellHeight),
            new Rectangle2D.Double(this.offsetX + (3 * cellWidth), this.offsetY + (3 * cellHeight), cellWidth, cellHeight),
            new Rectangle2D.Double(this.offsetX + (30 * cellWidth), this.offsetY + (3 * cellHeight), cellWidth, cellHeight),
            new Rectangle2D.Double(this.offsetX + (3 * cellWidth), this.offsetY + (9 * cellHeight), cellWidth, cellHeight),
            new Rectangle2D.Double(this.offsetX + (30 * cellWidth), this.offsetY + (9 * cellHeight), cellWidth, cellHeight),
            new Rectangle2D.Double(this.offsetX + (9 * cellWidth), this.offsetY + (3 * cellHeight), 7 * cellWidth, cellHeight),
            new Rectangle2D.Double(this.offsetX + (18 * cellWidth), this.offsetY + (3 * cellHeight), 7 * cellWidth, cellHeight),
            new Rectangle2D.Double(this.offsetX + (1 * cellWidth), this.offsetY + (6 * cellHeight), 3 * cellWidth, cellHeight),
            new Rectangle2D.Double(this.offsetX + (30 * cellWidth), this.offsetY + (6 * cellHeight), 3 *cellWidth,	cellHeight),
            new Rectangle2D.Double(this.offsetX + (15 *	cellWidth),	this.offsetY + (6 *	cellHeight),	4 *	cellWidth,	4 *	cellHeight),
            new Rectangle2D.Double(this.offsetX + (12 *	cellWidth),	this.offsetY + (12 *	cellHeight),	10 *	cellWidth,	1 *	cellHeight),
            new Rectangle2D.Double(this.offsetX + (1 * cellWidth), this.offsetY + (12 * cellHeight), 6 * cellWidth, 1 * cellHeight),
            new Rectangle2D.Double(this.offsetX + (27 * cellWidth), this.offsetY + (12 * cellHeight), 6 * cellWidth, 1 * cellHeight),
            new Rectangle2D.Double(this.offsetX + (12 * cellWidth), this.offsetY + (15 * cellHeight), 10 * cellWidth, 4 * cellHeight),
            new Rectangle2D.Double(this.offsetX + (3 * cellWidth), this.offsetY + (15 * cellHeight), 7 * cellWidth, 1 * cellHeight),
            new Rectangle2D.Double(this.offsetX + (24 * cellWidth), this.offsetY + (15 * cellHeight), 7 * cellWidth, 1 * cellHeight),
            new Rectangle2D.Double(this.offsetX + (3 * cellWidth), this.offsetY + (18 * cellHeight), 7 * cellWidth, 1 * cellHeight),
            new Rectangle2D.Double(this.offsetX + (24 * cellWidth), this.offsetY + (18 * cellHeight), 7 * cellWidth, 1 *	cellHeight),
            new Rectangle2D.Double(this.offsetX + (1 *	cellWidth),	this.offsetY + (21 *	cellHeight),	6 *	cellWidth,	1 *	cellHeight),
            new Rectangle2D.Double(this.offsetX + (27 *	cellWidth),	this.offsetY + (21 *	cellHeight),	6 *	cellWidth,	1 *	cellHeight),
            new Rectangle2D.Double(this.offsetX + (12 *	cellWidth),	this.offsetY + (21 *	cellHeight),	10 *	cellWidth,	1 *	cellHeight),
            new Rectangle2D.Double(this.offsetX + (15 * cellWidth), this.offsetY + (24 * cellHeight), 4 * cellWidth, 4 * cellHeight),
            new Rectangle2D.Double(this.offsetX + (1 * cellWidth), this.offsetY + (27 * cellHeight), 3 * cellWidth, cellHeight),
            new Rectangle2D.Double(this.offsetX + (30 * cellWidth), this.offsetY + (27 * cellHeight), 3 * cellWidth, cellHeight),
            new Rectangle2D.Double(this.offsetX + (9 * cellWidth), this.offsetY + (30 * cellHeight), 7 * cellWidth, cellHeight),
            new Rectangle2D.Double(this.offsetX + (18 * cellWidth), this.offsetY + (30 * cellHeight), 7 * cellWidth, cellHeight),
            new Rectangle2D.Double(this.offsetX + (3 * cellWidth), this.offsetY + (24 * cellHeight), cellWidth, cellHeight),
            new Rectangle2D.Double(this.offsetX + (30 * cellWidth), this.offsetY + (24 * cellHeight),	cellWidth,	cellHeight),
            new Rectangle2D.Double(this.offsetX + (3 *	cellWidth),	this.offsetY + (30 *	cellHeight),	cellWidth,	cellHeight),
            new Rectangle2D.Double(this.offsetX + (30 *	cellWidth),	this.offsetY + (30 *	cellHeight),	cellWidth,	cellHeight),
            new Rectangle2D.Double(this.offsetX + (6 *	cellWidth),	this.offsetY + (1 *	cellHeight),	cellWidth,	3 *	cellHeight),
            new Rectangle2D.Double(this.offsetX + (6 * cellWidth), this.offsetY + (6 * cellHeight), cellWidth, 6 * cellHeight),
            new Rectangle2D.Double(this.offsetX + (6 * cellWidth), this.offsetY + (22 * cellHeight), cellWidth, 6 * cellHeight),
            new Rectangle2D.Double(this.offsetX + (6 * cellWidth), this.offsetY + (30 * cellHeight), cellWidth, 3 * cellHeight),
            new Rectangle2D.Double(this.offsetX + (9 * cellWidth), this.offsetY + (6 * cellHeight), cellWidth, 9 * cellHeight),
            new Rectangle2D.Double(this.offsetX + (9 * cellWidth), this.offsetY + (19 * cellHeight), cellWidth, 9 * cellHeight),
            new Rectangle2D.Double(this.offsetX + (12 * cellWidth), this.offsetY + (4 * cellHeight), cellWidth, 6 * cellHeight),
            new Rectangle2D.Double(this.offsetX + (12 * cellWidth), this.offsetY + (24 * cellHeight), cellWidth, 6 * cellHeight),
            new Rectangle2D.Double(this.offsetX + (21 *	cellWidth),	this.offsetY + (4 *	cellHeight),	cellWidth,	6 *	cellHeight),
            new Rectangle2D.Double(this.offsetX + (21 *	cellWidth),	this.offsetY + (24 *	cellHeight),	cellWidth,	6 *	cellHeight),
            new Rectangle2D.Double(this.offsetX + (24 *	cellWidth),	this.offsetY + (6 *	cellHeight),	cellWidth,	9 *	cellHeight),
            new Rectangle2D.Double(this.offsetX + (24 * cellWidth), this.offsetY + (19 * cellHeight), cellWidth, 9 * cellHeight),
            new Rectangle2D.Double(this.offsetX + (27 * cellWidth), this.offsetY + (1 * cellHeight), cellWidth, 3 * cellHeight),
            new Rectangle2D.Double(this.offsetX + (27 * cellWidth), this.offsetY + (6 * cellHeight), cellWidth, 6 * cellHeight),
            new Rectangle2D.Double(this.offsetX + (27 * cellWidth), this.offsetY + (22 * cellHeight), cellWidth, 6 * cellHeight),
            new Rectangle2D.Double(this.offsetX + (27 * cellWidth), this.offsetY + (30 * cellHeight), cellWidth, 3 * cellHeight)
        };
        
        this.createBackground();
    }

    /**
     * Toggles the visibility of collision borders on the background. When enabled,
     * the
     * background will be regenerated to include visual indicators for collision
     * areas, which can
     * be useful for debugging purposes.
     */
    public void toggleCollisionBorders() {
        this.showCollisionBorders = !this.showCollisionBorders;
        this.createBackground();
    }

    public boolean colliding(Rectangle2D player) {
        for (Shape shape : collisions) {
            if (shape.intersects(player)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Generates the background image based on the scenario data and stores it in
     * the
     * image field. This method is called whenever the background needs to be
     * updated, such as when toggling collision borders.
     */
    private void createBackground() {

        image = new BufferedImage(Game.CANVAS_WIDTH, Game.CANVAS_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D) image.getGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int rows = scenario.length;
        int cols = scenario[0].length;
        int x = this.offsetX;
        int y = this.offsetY;

        g2d.setColor(new Color(56, 45, 162));
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (scenario[row][col] == 1) {
                    g2d.fillRect(x + (col * cellWidth), y + (row * cellHeight), cellWidth, cellHeight);
                } else if (scenario[row][col] == 5) {
                    g2d.fill(new NewRoundRectangle2D(x + (col * cellWidth), y + (row * cellHeight), cellWidth,
                            cellHeight, 10, 10, 10, 10, 10, 10, 10, 10));
                } else if (scenario[row][col] == 6) {
                    g2d.fill(new NewRoundRectangle2D(x + (col * cellWidth), y + (row * cellHeight), cellWidth,
                            cellHeight, 0, 0, 0, 0, 12, 12, 12, 12));
                } else if (scenario[row][col] == 7) {
                    g2d.fill(new NewRoundRectangle2D(x + (col * cellWidth), y + (row * cellHeight), cellWidth,
                            cellHeight, 12, 12, 12, 12, 0, 0, 0, 0));
                } else if (scenario[row][col] == 8) {
                    g2d.fill(new NewRoundRectangle2D(x + (col * cellWidth), y + (row * cellHeight), cellWidth,
                            cellHeight, 0, 0, 12, 12, 0, 0, 12, 12));
                } else if (scenario[row][col] == 9) {
                    g2d.fill(new NewRoundRectangle2D(x + (col * cellWidth), y + (row * cellHeight), cellWidth,
                            cellHeight, 12, 12, 0, 0, 12, 12, 0, 0));
                } else if (scenario[row][col] == 2) {
                    g2d.fill(new NewRoundRectangle2D(x + (col * cellWidth), y + (row * cellHeight), cellWidth,
                            cellHeight, 12, 12, 0, 0, 0, 0, 0, 0));
                } else if (scenario[row][col] == 3) {
                    g2d.fill(new NewRoundRectangle2D(x + (col * cellWidth), y + (row * cellHeight), cellWidth,
                            cellHeight, 0, 0, 12, 12, 0, 0, 0, 0));
                } else if (scenario[row][col] == 4) {
                    g2d.fill(new NewRoundRectangle2D(x + (col * cellWidth), y + (row * cellHeight), cellWidth,
                            cellHeight, 0, 0, 0, 0, 12, 12, 0, 0));
                } else if (scenario[row][col] == 11) {
                    g2d.fill(new NewRoundRectangle2D(x + (col * cellWidth), y + (row * cellHeight), cellWidth,
                            cellHeight, 0, 0, 0, 0, 0, 0, 12, 12));
                }
            }
        }

        if (this.showCollisionBorders) {
            g2d.setColor(Color.PINK);

            for (Shape collision : collisions) {
                g2d.draw(collision);
            }
        }

        g2d.dispose();
    }

    /**
     * Returns an array of Rectangle2D objects representing the collision shapes in
     * the
     * background. These shapes are used for collision detection with the player and
     * other
     * entities in the game.
     * 
     * @return
     */
    public BufferedImage getImage() {
        return image;
    }

    public void render(Graphics2D g2d) {
        g2d.drawImage(image, 0, 0, null);
    }
}