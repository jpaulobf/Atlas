package game;

import engine.Game;

import java.awt.Color;
import java.awt.Graphics;
import audio.Audio;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.Rectangle;


/**
 * The Atlas game.
 */
public class Atlas extends Game {

    private List<Entity> entities = new ArrayList<>();
    private Random random = new Random();

    public Atlas() {
        super("Atlas Game", 1600, 900, 60);
    }

    @Override
    public void init() {
        entities.clear();
        
        // Cores variadas
        Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.CYAN, 
                          Color.MAGENTA, Color.YELLOW, Color.PINK, Color.WHITE, Color.LIGHT_GRAY};

        // Gera 10 Quadrados e depois 10 Círculos
        for (int type = 0; type < 2; type++) { // 0 = Square, 1 = Circle
            int count = 0;
            while (count < 3) {
                int size = 80;
                // Tenta gerar uma posição aleatória dentro da área segura
                float newX = random.nextFloat() * (CANVAS_WIDTH - size);
                float newY = random.nextFloat() * (CANVAS_HEIGHT - size);
                
                // Verifica se sobrepõe alguma entidade já existente
                boolean overlapping = false;
                Rectangle newRect = new Rectangle((int)newX, (int)newY, size, size);
                
                for (Entity e : entities) {
                    Rectangle existing = new Rectangle((int)e.x, (int)e.y, e.width, e.height);
                    if (newRect.intersects(existing)) {
                        overlapping = true;
                        break;
                    }
                }

                if (!overlapping) {
                    float speedBase = (random.nextInt(14) + 7) * 30.0f; 
                    double angle = random.nextDouble() * 2 * Math.PI;
                    float vx = (float) (Math.cos(angle) * speedBase);
                    float vy = (float) (Math.sin(angle) * speedBase);
                    Color color = colors[entities.size() % colors.length];

                    if (type == 0) {
                        entities.add(new Square(newX, newY, size, size, vx, vy, color));
                    } else {
                        entities.add(new Circle(newX, newY, size, size, vx, vy, color));
                    }
                    count++;
                }
            }
        }
    }


    @Override
    public void onUpdate(long deltaTime) {
        for (Entity e : entities) {
            e.update(deltaTime, CANVAS_WIDTH, CANVAS_HEIGHT);
        }
        
        // Verificar colisão entre todas as entidades
        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                checkCollision(entities.get(i), entities.get(j));
            }
        }
    }

    @Override
    public void onRender(Graphics g, double interpolation) {
        for (Entity e : entities) {
            e.render(g, interpolation);
        }
    }

    /**
     * Verifica e resolve colisão entre duas entidades
     */
    private void checkCollision(Entity s1, Entity s2) {
        // Colisão AABB simples
        if (s1.x < s2.x + s2.width && s1.x + s1.width > s2.x &&
            s1.y < s2.y + s2.height && s1.y + s1.height > s2.y) {
            
            // Toca o som (apenas em um para não saturar)
            s1.playBoing();

            // Determinar a direção da colisão calculando a sobreposição
            float overlapX = Math.min(s1.x + s1.width, s2.x + s2.width) - Math.max(s1.x, s2.x);
            float overlapY = Math.min(s1.y + s1.height, s2.y + s2.height) - Math.max(s1.y, s2.y);

            // Se overlapX < overlapY, a colisão foi lateral (horizontal)
            if (overlapX < overlapY) {
                // Resolve overlap empurrando para fora (metade para cada)
                if (s1.x < s2.x) {
                    s1.x -= overlapX / 2;
                    s2.x += overlapX / 2;
                } else {
                    s1.x += overlapX / 2;
                    s2.x -= overlapX / 2;
                }
                
                // Inverte velocidades X
                s1.velX *= -1;
                s2.velX *= -1;
                
                // Aciona deformação X
                s1.triggerSquashX();
                s2.triggerSquashX();
            } else {
                // Colisão Vertical
                // Resolve overlap
                if (s1.y < s2.y) {
                    s1.y -= overlapY / 2;
                    s2.y += overlapY / 2;
                } else {
                    s1.y += overlapY / 2;
                    s2.y -= overlapY / 2;
                }
                
                // Inverte velocidades Y
                s1.velY *= -1;
                s2.velY *= -1;
                
                // Aciona deformação Y
                s1.triggerSquashY();
                s2.triggerSquashY();
            }
        }
    }

    // --- Abstract Base Entity ---
    abstract class Entity {
        float x, y, prevX, prevY;
        float velX, velY;
        int width, height;
        Color color;
        Audio audio;

        // Variáveis de deformação
        float scaleX = 1.0f;
        float scaleY = 1.0f;
        float squashCycleX = 0.0f;
        float squashCycleY = 0.0f;
        float squashSpeed = 15.0f;

        public Entity(float x, float y, int w, int h, float vx, float vy, Color c) {
            this.x = x;
            this.y = y;
            this.prevX = x;
            this.prevY = y;
            this.width = w;
            this.height = h;
            this.velX = vx;
            this.velY = vy;
            this.color = c;
            // Cada quadrado tem seu áudio independente
            this.audio = new Audio("/audio/boing.ogg");
        }

        public void update(long deltaTime, int canvasWidth, int canvasHeight) {
            prevX = x;
            prevY = y;
            double deltaSeconds = deltaTime / 1_000_000_000.0;

            // Atualiza animações de squash
            updateSquash(deltaSeconds);

            // Movimento
            x += velX * deltaSeconds;
            y += velY * deltaSeconds;

            // Colisão com Paredes
            if (x < 0) {
                x = 0;
                velX *= -1;
                triggerSquashX();
                playBoing();
            } else if (x + width > canvasWidth) {
                x = canvasWidth - width;
                velX *= -1;
                triggerSquashX();
                playBoing();
            }

            if (y < 0) {
                y = 0;
                velY *= -1;
                triggerSquashY();
                playBoing();
            } else if (y + height > canvasHeight) {
                y = canvasHeight - height;
                velY *= -1;
                triggerSquashY();
                playBoing();
            }
        }

        public abstract void render(Graphics g, double interpolation);

        private void updateSquash(double deltaSeconds) {
            if (squashCycleX > 0) {
                squashCycleX += squashSpeed * deltaSeconds;
                if (squashCycleX >= Math.PI) {
                    squashCycleX = 0;
                    scaleX = 1.0f;
                } else {
                    scaleX = 1.0f - 0.3f * (float) Math.sin(squashCycleX);
                }
            }
            if (squashCycleY > 0) {
                squashCycleY += squashSpeed * deltaSeconds;
                if (squashCycleY >= Math.PI) {
                    squashCycleY = 0;
                    scaleY = 1.0f;
                } else {
                    scaleY = 1.0f - 0.3f * (float) Math.sin(squashCycleY);
                }
            }
        }

        public void triggerSquashX() {
            if (squashCycleX == 0) squashCycleX = 0.01f;
        }

        public void triggerSquashY() {
            if (squashCycleY == 0) squashCycleY = 0.01f;
        }

        public void playBoing() {
            audio.play();
        }
    }

    // --- Concrete Implementations ---
    
    class Square extends Entity {
        public Square(float x, float y, int w, int h, float vx, float vy, Color c) {
            super(x, y, w, h, vx, vy, c);
        }

        @Override
        public void render(Graphics g, double interpolation) {
            float interpX = (float) (prevX + (x - prevX) * interpolation);
            float interpY = (float) (prevY + (y - prevY) * interpolation);

            float drawnWidth = width * scaleX;
            float drawnHeight = height * scaleY;

            float drawX = interpX + (width - drawnWidth) / 2.0f;
            float drawY = interpY + (height - drawnHeight) / 2.0f;

            g.setColor(color);
            g.fillRect((int) drawX, (int) drawY, (int) drawnWidth, (int) drawnHeight);
        }
    }

    class Circle extends Entity {
        public Circle(float x, float y, int w, int h, float vx, float vy, Color c) {
            super(x, y, w, h, vx, vy, c);
        }

        @Override
        public void render(Graphics g, double interpolation) {
            float interpX = (float) (prevX + (x - prevX) * interpolation);
            float interpY = (float) (prevY + (y - prevY) * interpolation);

            float drawnWidth = width * scaleX;
            float drawnHeight = height * scaleY;

            float drawX = interpX + (width - drawnWidth) / 2.0f;
            float drawY = interpY + (height - drawnHeight) / 2.0f;

            g.setColor(color);
            g.fillOval((int) drawX, (int) drawY, (int) drawnWidth, (int) drawnHeight);
        }
    }

    public static void main(String[] args) {
        new Atlas().start();
    }
}
