# Atlas Java 2D Engine

Uma Engine de Jogos 2D leve e de alta performance, construída "do zero" utilizando Java padrão (AWT/Swing). O foco do projeto é arquitetura limpa, performance e controle total sobre o ciclo de renderização.

## Funcionalidades

*   **Game Loop Profissional**: Implementação de *Fixed Timestep*. A lógica do jogo (física) roda em uma frequência fixa (UPS), enquanto a renderização (FPS) é desacoplada, permitindo taxas de quadros ilimitadas ou travadas (VSync/Cap).
*   **Render Interpolation**: Sistema de interpolação linear (`prevPos` vs `currPos`) para garantir movimento ultra-suave visualmente, independente da taxa de atualização do monitor.
*   **Active Rendering**: Utiliza `BufferStrategy` (Triple Buffering) diretamente em um `java.awt.Canvas`, ignorando o ciclo passivo de repintura do Swing (`ignoreRepaint`) para máxima performance e eliminação de *flickering*.
*   **Gerenciamento de Tela**:
    *   Suporte a **Windowed Mode**.
    *   Suporte a **Borderless Fullscreen** (Janela sem bordas maximizada).
    *   Suporte a **Exclusive Fullscreen** (Controle total do dispositivo gráfico).
    *   Alternância robusta em tempo real (ALT+ENTER) com tratamento de perda de contexto gráfico (*Context Loss*).
*   **Input System**: Gerenciamento de teclado via `KeyListener` com correções de foco para evitar perda de input durante trocas de janela.

## Demo Atual

O projeto inclui uma demonstração (`game.Atlas`) com um "personagem" (retângulo) controlável.

### Controles

*   **W, A, S, D**: Movimentação do personagem (com física baseada em `deltaTime`).
*   **ALT + ENTER**: Alternar entre Tela Cheia e Modo Janela.
*   **ESC**: Fechar o jogo.

## Arquitetura

A engine está organizada para separar o "Motor" do "Jogo":

### Pacote `engine`
*   **`Engine`**: O coração do loop. Gerencia o tempo, acumuladores de lag e chama `update()` e `render()`.
*   **`Game`**: Classe abstrata que serve de Template. Define o fluxo de inicialização e fornece acesso à janela e inputs.

### Pacote `engine.window`
*   **`GameWindow`**: Encapsula o `JFrame` e o `Canvas`. Lida com a complexidade de criar/destruir janelas nativas ao trocar de modos de tela e reinvindicar foco.
*   **`ScreenState`**: Enumeração dos estados de tela suportados.

### Pacote `engine.input`
*   **`KeyboardInputs`**: Listener responsável por mapear o estado atual das teclas.

### Pacote `game`
*   **`Atlas`**: A implementação concreta do jogo. Herda de `Game` e implementa a lógica específica (`onUpdate`, `onRender`).

## Como Usar

Para criar um novo jogo usando a engine:

1.  Crie uma classe que estenda `engine.Game`.
2.  Implemente os métodos abstratos:
    *   `init()`: Carregamento de recursos.
    *   `onUpdate(long deltaTime)`: Lógica matemática/física.
    *   `onRender(Graphics g, double interpolation)`: Desenho na tela.
3.  No seu método `main`, instancie seu jogo e chame `.start()`.

---