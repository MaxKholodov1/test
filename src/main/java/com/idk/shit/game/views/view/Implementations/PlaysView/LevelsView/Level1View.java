package com.idk.shit.game.views.view.Implementations.PlaysView.LevelsView;

import java.util.ArrayDeque;
import java.util.Deque;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

import com.idk.shit.game.state.ValueObjects.Implementations.Plays.Levels.Level1;
import com.idk.shit.game.views.view.Implementations.PlaysView.PlayingView;
import com.idk.shit.graphics.Texture;
import com.idk.shit.graphics.TextureCache;
import com.idk.shit.game.state.StateManager;

import com.idk.shit.game.state.ValueObjects.objects.Object;
import com.idk.shit.game.state.ValueObjects.objects.Player;
import com.idk.shit.ui.Button;
import com.idk.shit.ui.TextRenderer;
import com.idk.shit.utils.Colours;
import com.idk.shit.utils.InputManager;
import com.idk.shit.utils.ScoreManager;

public class Level1View extends PlayingView {
    private Deque<Object> blocks = new ArrayDeque<>();
    public float screen_height=1000;
    public float screen_width=650;
    public float RATIO = screen_width / screen_height;
    public Player player;
    private int bestscore;
    private int score = 0;
    private final Button redButton;
    private Texture playerTexture;
    private Texture blockTexture;
    private final Texture background;
    private final Level1 level1;
    public Level1View (StateManager stateManager, Level1 level1, long window, InputManager inputManager, long vg, TextRenderer textRenderer, ScoreManager scoreManager) {
        super(stateManager, window, inputManager, vg, textRenderer, scoreManager); // Передаем window в родительский класс
        this.level1 = level1;
        redButton = new Button(-0.45f, 0.9f, 0.4f, 0.1f, "menu", Colours.GREEN, vg, textRenderer);
        background = TextureCache.getTexture("night-star-sky-night-sky-preview.jpg");
        initGame();
    }
    private void initGame() {
        playerTexture = TextureCache.getTexture("pngegg.png");
        blockTexture = TextureCache.getTexture("cloud.png");
        bestscore = scoreManager.getHighScore(1);
    }
    
    public void  update() throws Exception {
        redButton.update(this.window);
        if (redButton.isClicked()||inputManager.isKeyPressed(GLFW_KEY_SPACE)) {
            stateManager.Menu();
            cleanup();
            inputManager.cleanup();
        }
        player = level1.getPlayer();
        if (player.fall_down()){
            stateManager.GameOver();
            cleanup();
            inputManager.cleanup();
        }
    }
    public void render() {
        background.draw(0f, 0f, 2*RATIO, 2);
        blocks = level1.getBlocks();
        for (Object block : blocks) {
            blockTexture.draw(block.getX(), block.getY(),block.getWidth(), block.getHeight());
        }
        redButton.draw();
        player = level1.getPlayer();
        playerTexture.draw(player.getX(), player.getY(),player.getWidth(), player.getHeight());
        score = level1.getScore();
        String scoreString = String.valueOf(score);
        String bestscoreString = String.valueOf(bestscore);
        textRenderer.drawText(RATIO-0.1f, 0.9f, scoreString, Colours.WHITE, vg, 0.5f, 0.5f);
        textRenderer.drawText(RATIO - 0.1f, 0.8f, bestscoreString, Colours.ORANGE, vg, 0.15f, 0.15f);
    }
    public void cleanup() { level1.cleanup(); }
    public int GetLevel(){
        return 1;
    }
    public int GetScore(){
        return score;
    }
}
