package com.idk.shit;

import org.lwjgl.Version;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.nanovg.NanoVGGL3;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import com.idk.shit.game.state.StateManager;
import com.idk.shit.game.views.ViewManager;
import com.idk.shit.game.views.view.Implementations.MenuView;
import com.idk.shit.utils.InputManager;
import com.idk.shit.graphics.Texture;
import com.idk.shit.graphics.TextureCache;
import com.idk.shit.ui.TextRenderer;
import com.idk.shit.utils.ScoreManager;

public class Main {
    private long window;
    private ViewManager viewManager;
    protected InputManager inputManager;
    protected int screenWidth = 650;
    protected int screenHeight = 1000;
    protected float ratio;
    protected MenuView menuView;
    protected StateManager stateManager;
    protected long vg;
    private Texture playerTexture, blockTexture;
    private TextRenderer textRenderer;
    private ScoreManager scoreManager;

    // Переменные для управления временем
    private long lastFrameTime;
    private final long NANOSECONDS_PER_FRAME = 1_000_000_000L / 60; // 60 FPS

    public void run() {
        System.out.println("Hello, LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Освобождение ресурсов окна и завершение GLFW
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        // Устанавливаем обработчик ошибок GLFW
        GLFWErrorCallback.createPrint(System.err).set();

        // Инициализация GLFW
        if (!glfwInit())
            throw new IllegalStateException("Не удалось инициализировать GLFW");

        // Настройки GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        // Создание окна
        this.window = glfwCreateWindow(screenWidth, screenHeight, "Jump", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Ошибка создания окна GLFW");

        this.inputManager = new InputManager();
        inputManager.registerCallbacks(window);

        // Устанавливаем текущий контекст OpenGL
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1); // Включаем вертикальную синхронизацию (V-Sync)
        glfwShowWindow(window);
        GL.createCapabilities();

        ratio = (float) screenWidth / (float) screenHeight;
        glMatrixMode(GL_PROJECTION);
        glOrtho(-ratio, ratio, -1, 1, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        // Инициализация NanoVG
        vg = NanoVGGL3.nvgCreate(NanoVG.NVG_ALIGN_BASELINE);
        textRenderer = new TextRenderer(vg);

        // Инициализация менеджера рекордов
        scoreManager = new ScoreManager("scores.txt");
        System.out.println("High Score: " + scoreManager.getHighScore(1));

        // Загрузка текстур
        playerTexture = TextureCache.getTexture("pngegg.png");
        blockTexture = TextureCache.getTexture("трава.png");

        // Инициализация состояния игры и менеджера представлений
        stateManager = new StateManager(inputManager, scoreManager);
        viewManager = new ViewManager(window, inputManager, stateManager, vg, textRenderer, scoreManager);
        menuView = new MenuView(stateManager, window, inputManager, vg, textRenderer, scoreManager);
        viewManager.setState(menuView);
    }

    private void loop() {
        GL.createCapabilities();
        glClearColor(1.0f, 1.0f, 1.0f, 0.0f); // Устанавливаем цвет очистки экрана (белый фон)

        // Инициализация времени
        lastFrameTime = System.nanoTime();

        // Основной цикл рендеринга
        while (!glfwWindowShouldClose(window)) {
            // Очистка буферов
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // Обновление и рендеринг
            stateManager.update();
            viewManager.update();
            viewManager.render();

            // Обмен буферов
            glfwSwapBuffers(window);

            // Обработка событий
            glfwPollEvents();

            // Управление временем для фиксированного FPS
            long currentTime = System.nanoTime();
            long elapsedTime = currentTime - lastFrameTime;

            if (elapsedTime < NANOSECONDS_PER_FRAME) {
                long sleepTime = (NANOSECONDS_PER_FRAME - elapsedTime) / 1_000_000; // Переводим в миллисекунды
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            lastFrameTime = currentTime;
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }
}