package game;

import engine.Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import audio.Audio;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The Atlas game.
 */
public class Atlas extends Game {

    private List<Entity> entities = new ArrayList<>();
    private Random random = new Random();
    private int level = 10; // Nível de dificuldade (1 a 10)
    private BufferedImage background; // O Cache do cenário

    // Configurações visuais e lógicas das faixas
    private final int TOP_SAFE_ZONE = 100;
    private final int BOTTOM_SAFE_ZONE = 100;
    private final int MIDDLE_SAFE_ZONE = 120;

    public Atlas() {
        super("Atlas Game", 1600, 900, 120);
    }

    @Override
    public void init() {
        entities.clear();
        createBackground(); // Gera a imagem do cenário uma única vez
        
        // Cores variadas
        Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.CYAN, 
                          Color.MAGENTA, Color.YELLOW, Color.PINK, Color.WHITE, Color.LIGHT_GRAY};

        int totalHeight = CANVAS_HEIGHT;
        
        // Altura disponível para as pistas (Total - Zonas Seguras)
        int availableRoadHeight = totalHeight - TOP_SAFE_ZONE - BOTTOM_SAFE_ZONE - MIDDLE_SAFE_ZONE;

        // São 8 faixas no total (4 em cima, 4 em baixo)
        int laneHeight = availableRoadHeight / 8;
        int carSize = 60; // Um pouco menor que a faixa
        int carYOffset = (laneHeight - carSize) / 2; // Centralizar na faixa

        // Criar as faixas
        for (int lane = 0; lane < 8; lane++) {
            // Calcular posição Y da faixa
            float laneY;
            if (lane < 4) {
                // Faixas superiores (0 a 3)
                laneY = TOP_SAFE_ZONE + (lane * laneHeight);

            } else {
                // Faixas inferiores (4 a 7) - Pula a middle zone
                laneY = TOP_SAFE_ZONE + (4 * laneHeight) + MIDDLE_SAFE_ZONE + ((lane - 4) * laneHeight);
            }

            // Definir velocidade e direção da faixa
            // Faixas pares vão para direita, ímpares para esquerda (exemplo)
            boolean movingRight = (lane % 2 == 0);
            
            // Velocidade escala com o nível (De 150px/s no Lvl 1 até ~600px/s no Lvl 10)
            float baseSpeed = 150.0f + (level * 40.0f); 
            float speed = baseSpeed + random.nextFloat() * 100.0f; // Variação aleatória
            if (!movingRight) speed *= -1;

            // Quantidade de carros escala com o nível (Mais difícil = mais carros)
            // Level 1-2: 1 carro. Level 10: até 4 carros.
            int maxCars = 1 + (int)Math.ceil(level / 3.0);
            int carsInLane = random.nextInt(maxCars) + 1;
            
            List<Entity> laneEntities = new ArrayList<>(); // Lista temporária para checar sobreposição na faixa atual

            for (int i = 0; i < carsInLane; i++) {
                float startX = 0;
                boolean overlaps = true;
                int attempts = 0;

                // Tenta encontrar uma posição livre (máximo de 50 tentativas para não travar o loop)
                while (overlaps && attempts < 50) {
                    startX = random.nextInt(CANVAS_WIDTH);
                    overlaps = false;

                    for (Entity other : laneEntities) {
                        // Verifica se está muito perto de outro carro (Gap de 50px)
                        float safeGap = 50.0f;
                        if (startX < other.x + other.width + safeGap && startX + carSize + safeGap > other.x) {
                            overlaps = true;
                            break;
                        }
                    }
                    attempts++;
                }
                
                // Só adiciona se encontrou um lugar seguro
                if (!overlaps) {
                    Color color = colors[random.nextInt(colors.length)];
                    
                    Entity car = random.nextBoolean() ? 
                        new Square(startX, laneY + carYOffset, carSize, carSize, speed, 0, color) :
                        new Circle(startX, laneY + carYOffset, carSize, carSize, speed, 0, color);
                    
                    entities.add(car);
                    laneEntities.add(car);
                }
            }
        }
    }

    @Override
    public void onUpdate(long deltaTime) {
        for (Entity e : entities) {
            e.update(deltaTime, CANVAS_WIDTH, CANVAS_HEIGHT);
        }
        // Removemos a colisão entre carros para simular tráfego fluído (Freeway style)
        // Se quiser que eles batam, podemos reativar depois.
    }

    @Override
    public void onRender(Graphics g, double interpolation) {
        if (background != null) {
            g.drawImage(background, 0, 0, null);
        }

        // --- Desenhar Entidades ---
        for (Entity e : entities) {
            e.render(g, interpolation);
        }
    }

    /**
     * Renderiza o cenário estático em um BufferedImage para otimizar o onRender.
     */
    private void createBackground() {
        background = new BufferedImage(CANVAS_WIDTH, CANVAS_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics g = background.getGraphics();

        int availableRoadHeight = CANVAS_HEIGHT - TOP_SAFE_ZONE - BOTTOM_SAFE_ZONE - MIDDLE_SAFE_ZONE;
        int laneHeight = availableRoadHeight / 8;
        int roadHeightBlock = laneHeight * 4;

        // 1. Zonas Seguras (Grama/Verde)
        g.setColor(new Color(34, 139, 34)); // Forest Green
        g.fillRect(0, 0, CANVAS_WIDTH, TOP_SAFE_ZONE); // Topo
        
        int middleY = TOP_SAFE_ZONE + roadHeightBlock;
        g.fillRect(0, middleY, CANVAS_WIDTH, MIDDLE_SAFE_ZONE); // Meio
        
        int bottomY = middleY + MIDDLE_SAFE_ZONE + roadHeightBlock;
        g.fillRect(0, bottomY, CANVAS_WIDTH, BOTTOM_SAFE_ZONE); // Base

        // 2. Estradas (Asfalto/Cinza)
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, TOP_SAFE_ZONE, CANVAS_WIDTH, roadHeightBlock); // Estrada Superior
        g.fillRect(0, middleY + MIDDLE_SAFE_ZONE, CANVAS_WIDTH, roadHeightBlock); // Estrada Inferior

        // 3. Linhas das faixas
        g.setColor(Color.WHITE);
        for (int i = 1; i < 4; i++) {
            int lineY = TOP_SAFE_ZONE + (i * laneHeight);
            g.fillRect(0, lineY, CANVAS_WIDTH, 3);

            lineY = middleY + MIDDLE_SAFE_ZONE + (i * laneHeight);
            g.fillRect(0, lineY, CANVAS_WIDTH, 3);
        }
        g.dispose(); // Libera o recurso gráfico temporário da imagem
    }

    // --- Abstract Base Entity ---
    abstract class Entity {
        float x, y, prevX, prevY;
        float velX, velY;
        int width, height;
        Color color;
        Audio audio;

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

            // Movimento
            x += velX * deltaSeconds;
            y += velY * deltaSeconds;

            // Lógica de "Wrap-around" (Atravessar a tela)
            // Se sair pela direita, volta na esquerda
            if (velX > 0 && x > canvasWidth) {
                x = -width;
                prevX = x; // Reseta o histórico para evitar interpolação cruzada (flicker)
            }
            
            // Se sair pela esquerda, volta na direita
            if (velX < 0 && x + width < 0) {
                x = canvasWidth;
                prevX = x; // Reseta o histórico para evitar interpolação cruzada (flicker)
            }
        }

        public abstract void render(Graphics g, double interpolation);

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

            g.setColor(color);
            g.fillRect((int) interpX, (int) interpY, (int) width, (int) height);
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

            g.setColor(color);
            g.fillOval((int) interpX, (int) interpY, (int) width, (int) height);
        }
    }

    public static void main(String[] args) {
        new Atlas().start();
    }
}
