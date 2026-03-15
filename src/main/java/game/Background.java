package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import engine.Game;
import util.NewRoundRectangle2D;

public class Background {

    public Background() {
        this.createBackground();
    }

    // Cache for the background image
    private BufferedImage image;

    private boolean showColisionBorders = false;

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

    
    public void toggleColisionBorders() {
        this.showColisionBorders = !this.showColisionBorders;
        this.createBackground();
    }

    private void createBackground() {

        image = new BufferedImage(Game.CANVAS_WIDTH, Game.CANVAS_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D)image.getGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int rows = scenario.length;
        int cols = scenario[0].length;
        int cellWidth = 24;
        int cellHeight = 24;
        int x = (Game.CANVAS_WIDTH / 2) - (cellWidth * 34 / 2);
        int y = 20;

        g2d.setColor(new Color(56, 45, 162));
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (scenario[row][col] == 1) {
                    g2d.fillRect(x + (col * cellWidth), y + (row * cellHeight), cellWidth, cellHeight);
                } else if (scenario[row][col] == 5) {
                    g2d.fill(new NewRoundRectangle2D(x + (col * cellWidth), y + (row * cellHeight), cellWidth, cellHeight, 10, 10, 10, 10, 10, 10, 10, 10));
                } else if (scenario[row][col] == 6) {
                    g2d.fill(new NewRoundRectangle2D(x + (col * cellWidth), y + (row * cellHeight), cellWidth, cellHeight, 0, 0, 0, 0, 12, 12, 12, 12));
                } else if (scenario[row][col] == 7) {
                    g2d.fill(new NewRoundRectangle2D(x + (col * cellWidth), y + (row * cellHeight), cellWidth, cellHeight, 12, 12, 12, 12, 0, 0, 0, 0));
                } else if (scenario[row][col] == 8) {
                    g2d.fill(new NewRoundRectangle2D(x + (col * cellWidth), y + (row * cellHeight), cellWidth, cellHeight, 0, 0, 12, 12, 0, 0, 12, 12));
                } else if (scenario[row][col] == 9) {
                    g2d.fill(new NewRoundRectangle2D(x + (col * cellWidth), y + (row * cellHeight), cellWidth, cellHeight, 12, 12, 0, 0, 12, 12, 0, 0));
                } else if (scenario[row][col] == 2) {
                    g2d.fill(new NewRoundRectangle2D(x + (col * cellWidth), y + (row * cellHeight), cellWidth, cellHeight, 12, 12, 0, 0, 0, 0, 0, 0));
                } else if (scenario[row][col] == 3) {
                    g2d.fill(new NewRoundRectangle2D(x + (col * cellWidth), y + (row * cellHeight), cellWidth, cellHeight, 0, 0, 12, 12, 0, 0, 0, 0));
                } else if (scenario[row][col] == 4) {
                    g2d.fill(new NewRoundRectangle2D(x + (col * cellWidth), y + (row * cellHeight), cellWidth, cellHeight, 0, 0, 0, 0, 12, 12, 0, 0));
                } else if (scenario[row][col] == 11) {
                    g2d.fill(new NewRoundRectangle2D(x + (col * cellWidth), y + (row * cellHeight), cellWidth, cellHeight, 0, 0, 0, 0, 0, 0, 12, 12));
                }
            }
        }

        Rectangle2D [] colisions = new Rectangle2D[] {
            new Rectangle2D.Double(x, y, 34 * cellWidth, cellHeight),
            new Rectangle2D.Double(x, y + (1 * cellHeight), cellWidth, 15 * cellHeight),
            new Rectangle2D.Double(x, y + (18 * cellHeight), cellWidth, 15 * cellHeight),
            new Rectangle2D.Double(x, y + (33 * cellHeight), 34 * cellWidth, cellHeight),
            new Rectangle2D.Double(x + (33 * cellWidth), y + (1 * cellHeight), cellWidth, 15 * cellHeight),
            new Rectangle2D.Double(x + (33 * cellWidth), y + (18 * cellHeight), cellWidth, 15 * cellHeight),
            new Rectangle2D.Double(x + (3 * cellWidth), y + (3 * cellHeight), cellWidth, cellHeight),
            new Rectangle2D.Double(x + (30 * cellWidth), y + (3 * cellHeight), cellWidth, cellHeight),
            new Rectangle2D.Double(x + (3 * cellWidth), y + (9 * cellHeight), cellWidth, cellHeight),
            new Rectangle2D.Double(x + (30 * cellWidth), y + (9 * cellHeight), cellWidth, cellHeight),        
            new Rectangle2D.Double(x + (9 * cellWidth), y + (3 * cellHeight), 7 * cellWidth, cellHeight),
            new Rectangle2D.Double(x + (18 * cellWidth), y + (3 * cellHeight), 7 * cellWidth, cellHeight),
            new Rectangle2D.Double(x + (1 * cellWidth), y + (6 * cellHeight), 3 * cellWidth, cellHeight),
            new Rectangle2D.Double(x + (30 * cellWidth), y + (6 * cellHeight), 3 * cellWidth, cellHeight),
            new Rectangle2D.Double(x + (15 * cellWidth), y + (6 * cellHeight), 4 * cellWidth, 4 * cellHeight),
            new Rectangle2D.Double(x + (12 * cellWidth), y + (12 * cellHeight), 10 * cellWidth, 1 * cellHeight),
            new Rectangle2D.Double(x + (1 * cellWidth), y + (12 * cellHeight), 6 * cellWidth, 1 * cellHeight),
            new Rectangle2D.Double(x + (27 * cellWidth), y + (12 * cellHeight), 6 * cellWidth, 1 * cellHeight),
            new Rectangle2D.Double(x + (12 * cellWidth), y + (15 * cellHeight), 10 * cellWidth, 4 * cellHeight),
            new Rectangle2D.Double(x + (3 * cellWidth), y + (15 * cellHeight), 7 * cellWidth, 1 * cellHeight),
            new Rectangle2D.Double(x + (24 * cellWidth), y + (15 * cellHeight), 7 * cellWidth, 1 * cellHeight),
            new Rectangle2D.Double(x + (3 * cellWidth), y + (18 * cellHeight), 7 * cellWidth, 1 * cellHeight),
            new Rectangle2D.Double(x + (24 * cellWidth), y + (18 * cellHeight), 7 * cellWidth, 1 * cellHeight),
            new Rectangle2D.Double(x + (1 * cellWidth), y + (21 * cellHeight), 6 * cellWidth, 1 * cellHeight),
            new Rectangle2D.Double(x + (27 * cellWidth), y + (21 * cellHeight), 6 * cellWidth, 1 * cellHeight),
            new Rectangle2D.Double(x + (12 * cellWidth), y + (21 * cellHeight), 10 * cellWidth, 1 * cellHeight),
            new Rectangle2D.Double(x + (15 * cellWidth), y + (24 * cellHeight), 4 * cellWidth, 4 * cellHeight),
            new Rectangle2D.Double(x + (1 * cellWidth), y + (27 * cellHeight), 3 * cellWidth, cellHeight),
            new Rectangle2D.Double(x + (30 * cellWidth), y + (27 * cellHeight), 3 * cellWidth, cellHeight),
            new Rectangle2D.Double(x + (9 * cellWidth), y + (30 * cellHeight), 7 * cellWidth, cellHeight),
            new Rectangle2D.Double(x + (18 * cellWidth), y + (30 * cellHeight), 7 * cellWidth, cellHeight),
            new Rectangle2D.Double(x + (3 * cellWidth), y + (24 * cellHeight), cellWidth, cellHeight),
            new Rectangle2D.Double(x + (30 * cellWidth), y + (24 * cellHeight), cellWidth, cellHeight),
            new Rectangle2D.Double(x + (3 * cellWidth), y + (30 * cellHeight), cellWidth, cellHeight),
            new Rectangle2D.Double(x + (30 * cellWidth), y + (30 * cellHeight), cellWidth, cellHeight),
            new Rectangle2D.Double(x + (6 * cellWidth), y + (1 * cellHeight), cellWidth, 3 * cellHeight),
            new Rectangle2D.Double(x + (6 * cellWidth), y + (6 * cellHeight), cellWidth, 6 * cellHeight),
            new Rectangle2D.Double(x + (6 * cellWidth), y + (22 * cellHeight), cellWidth, 6 * cellHeight),
            new Rectangle2D.Double(x + (6 * cellWidth), y + (30 * cellHeight), cellWidth, 3 * cellHeight),
            new Rectangle2D.Double(x + (9 * cellWidth), y + (6 * cellHeight), cellWidth, 9 * cellHeight),
            new Rectangle2D.Double(x + (9 * cellWidth), y + (19 * cellHeight), cellWidth, 9 * cellHeight),
            new Rectangle2D.Double(x + (12 * cellWidth), y + (4 * cellHeight), cellWidth, 6 * cellHeight),
            new Rectangle2D.Double(x + (12 * cellWidth), y + (24 * cellHeight), cellWidth, 6 * cellHeight),
            new Rectangle2D.Double(x + (21 * cellWidth), y + (4 * cellHeight), cellWidth, 6 * cellHeight),
            new Rectangle2D.Double(x + (21 * cellWidth), y + (24 * cellHeight), cellWidth, 6 * cellHeight),
            new Rectangle2D.Double(x + (24 * cellWidth), y + (6 * cellHeight), cellWidth, 9 * cellHeight),
            new Rectangle2D.Double(x + (24 * cellWidth), y + (19 * cellHeight), cellWidth, 9 * cellHeight),
            new Rectangle2D.Double(x + (27 * cellWidth), y + (1 * cellHeight), cellWidth, 3 * cellHeight),
            new Rectangle2D.Double(x + (27 * cellWidth), y + (6 * cellHeight), cellWidth, 6 * cellHeight),
            new Rectangle2D.Double(x + (27 * cellWidth), y + (22 * cellHeight), cellWidth, 6 * cellHeight),
            new Rectangle2D.Double(x + (27 * cellWidth), y + (30 * cellHeight), cellWidth, 3 * cellHeight)
        };

        if (this.showColisionBorders) {
            g2d.setColor(Color.PINK);

            for (Shape colision : colisions) {
                g2d.draw(colision);
            }

            /*
            //External borders
            g2d.draw(new Rectangle2D.Double(x, y, 34 * cellWidth, cellHeight));
            g2d.draw(new Rectangle2D.Double(x, y + (1 * cellHeight), cellWidth, 15 * cellHeight));
            g2d.draw(new Rectangle2D.Double(x, y + (18 * cellHeight), cellWidth, 15 * cellHeight));
            g2d.draw(new Rectangle2D.Double(x, y + (33 * cellHeight), 34 * cellWidth, cellHeight));
            g2d.draw(new Rectangle2D.Double(x + (33 * cellWidth), y + (1 * cellHeight), cellWidth, 15 * cellHeight));
            g2d.draw(new Rectangle2D.Double(x + (33 * cellWidth), y + (18 * cellHeight), cellWidth, 15 * cellHeight));

            //Bullets borders
            g2d.draw(new Rectangle2D.Double(x + (3 * cellWidth), y + (3 * cellHeight), cellWidth, cellHeight));
            g2d.draw(new Rectangle2D.Double(x + (30 * cellWidth), y + (3 * cellHeight), cellWidth, cellHeight));
            g2d.draw(new Rectangle2D.Double(x + (3 * cellWidth), y + (9 * cellHeight), cellWidth, cellHeight));
            g2d.draw(new Rectangle2D.Double(x + (30 * cellWidth), y + (9 * cellHeight), cellWidth, cellHeight));
            
            //Vertical borders
            g2d.draw(new Rectangle2D.Double(x + (9 * cellWidth), y + (3 * cellHeight), 7 * cellWidth, cellHeight));
            g2d.draw(new Rectangle2D.Double(x + (18 * cellWidth), y + (3 * cellHeight), 7 * cellWidth, cellHeight));

            g2d.draw(new Rectangle2D.Double(x + (1 * cellWidth), y + (6 * cellHeight), 3 * cellWidth, cellHeight));
            g2d.draw(new Rectangle2D.Double(x + (30 * cellWidth), y + (6 * cellHeight), 3 * cellWidth, cellHeight));

            g2d.draw(new Rectangle2D.Double(x + (15 * cellWidth), y + (6 * cellHeight), 4 * cellWidth, 4 * cellHeight));
            g2d.draw(new Rectangle2D.Double(x + (12 * cellWidth), y + (12 * cellHeight), 10 * cellWidth, 1 * cellHeight));

            g2d.draw(new Rectangle2D.Double(x + (1 * cellWidth), y + (12 * cellHeight), 6 * cellWidth, 1 * cellHeight));
            g2d.draw(new Rectangle2D.Double(x + (27 * cellWidth), y + (12 * cellHeight), 6 * cellWidth, 1 * cellHeight));

            g2d.draw(new Rectangle2D.Double(x + (12 * cellWidth), y + (15 * cellHeight), 10 * cellWidth, 4 * cellHeight));
            g2d.draw(new Rectangle2D.Double(x + (3 * cellWidth), y + (15 * cellHeight), 7 * cellWidth, 1 * cellHeight));
            g2d.draw(new Rectangle2D.Double(x + (24 * cellWidth), y + (15 * cellHeight), 7 * cellWidth, 1 * cellHeight));
            g2d.draw(new Rectangle2D.Double(x + (3 * cellWidth), y + (18 * cellHeight), 7 * cellWidth, 1 * cellHeight));
            g2d.draw(new Rectangle2D.Double(x + (24 * cellWidth), y + (18 * cellHeight), 7 * cellWidth, 1 * cellHeight));

            g2d.draw(new Rectangle2D.Double(x + (1 * cellWidth), y + (21 * cellHeight), 6 * cellWidth, 1 * cellHeight));
            g2d.draw(new Rectangle2D.Double(x + (27 * cellWidth), y + (21 * cellHeight), 6 * cellWidth, 1 * cellHeight));

            g2d.draw(new Rectangle2D.Double(x + (12 * cellWidth), y + (21 * cellHeight), 10 * cellWidth, 1 * cellHeight));
            g2d.draw(new Rectangle2D.Double(x + (15 * cellWidth), y + (24 * cellHeight), 4 * cellWidth, 4 * cellHeight));

            g2d.draw(new Rectangle2D.Double(x + (1 * cellWidth), y + (27 * cellHeight), 3 * cellWidth, cellHeight));
            g2d.draw(new Rectangle2D.Double(x + (30 * cellWidth), y + (27 * cellHeight), 3 * cellWidth, cellHeight));

            g2d.draw(new Rectangle2D.Double(x + (9 * cellWidth), y + (30 * cellHeight), 7 * cellWidth, cellHeight));
            g2d.draw(new Rectangle2D.Double(x + (18 * cellWidth), y + (30 * cellHeight), 7 * cellWidth, cellHeight));

            //Bullets borders
            g2d.draw(new Rectangle2D.Double(x + (3 * cellWidth), y + (24 * cellHeight), cellWidth, cellHeight));
            g2d.draw(new Rectangle2D.Double(x + (30 * cellWidth), y + (24 * cellHeight), cellWidth, cellHeight));
            g2d.draw(new Rectangle2D.Double(x + (3 * cellWidth), y + (30 * cellHeight), cellWidth, cellHeight));
            g2d.draw(new Rectangle2D.Double(x + (30 * cellWidth), y + (30 * cellHeight), cellWidth, cellHeight));

            //Horizontal borders
            g2d.draw(new Rectangle2D.Double(x + (6 * cellWidth), y + (1 * cellHeight), cellWidth, 3 * cellHeight));
            g2d.draw(new Rectangle2D.Double(x + (6 * cellWidth), y + (6 * cellHeight), cellWidth, 6 * cellHeight));
            g2d.draw(new Rectangle2D.Double(x + (6 * cellWidth), y + (22 * cellHeight), cellWidth, 6 * cellHeight));
            g2d.draw(new Rectangle2D.Double(x + (6 * cellWidth), y + (30 * cellHeight), cellWidth, 3 * cellHeight));

            g2d.draw(new Rectangle2D.Double(x + (9 * cellWidth), y + (6 * cellHeight), cellWidth, 9 * cellHeight));
            g2d.draw(new Rectangle2D.Double(x + (9 * cellWidth), y + (19 * cellHeight), cellWidth, 9 * cellHeight));

            g2d.draw(new Rectangle2D.Double(x + (12 * cellWidth), y + (4 * cellHeight), cellWidth, 6 * cellHeight));
            g2d.draw(new Rectangle2D.Double(x + (12 * cellWidth), y + (24 * cellHeight), cellWidth, 6 * cellHeight));
            g2d.draw(new Rectangle2D.Double(x + (21 * cellWidth), y + (4 * cellHeight), cellWidth, 6 * cellHeight));
            g2d.draw(new Rectangle2D.Double(x + (21 * cellWidth), y + (24 * cellHeight), cellWidth, 6 * cellHeight));

            g2d.draw(new Rectangle2D.Double(x + (24 * cellWidth), y + (6 * cellHeight), cellWidth, 9 * cellHeight));
            g2d.draw(new Rectangle2D.Double(x + (24 * cellWidth), y + (19 * cellHeight), cellWidth, 9 * cellHeight));

            g2d.draw(new Rectangle2D.Double(x + (27 * cellWidth), y + (1 * cellHeight), cellWidth, 3 * cellHeight));
            g2d.draw(new Rectangle2D.Double(x + (27 * cellWidth), y + (6 * cellHeight), cellWidth, 6 * cellHeight));
            g2d.draw(new Rectangle2D.Double(x + (27 * cellWidth), y + (22 * cellHeight), cellWidth, 6 * cellHeight));
            g2d.draw(new Rectangle2D.Double(x + (27 * cellWidth), y + (30 * cellHeight), cellWidth, 3 * cellHeight));
            */
        }

        g2d.dispose();
    }

    public BufferedImage getImage() {
        return image;
    }
}
