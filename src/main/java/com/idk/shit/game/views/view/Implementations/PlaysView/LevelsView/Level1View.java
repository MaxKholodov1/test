package com.idk.shit.game.views.view.Implementations.PlaysView.LevelsView;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

import com.idk.shit.game.state.ValueObjects.Implementations.Plays.Levels.Level1;
import com.idk.shit.graphics.Texture;
import com.idk.shit.graphics.TextureCache;
import com.idk.shit.game.state.State;
import com.idk.shit.game.views.view.ApplicationView;

import com.idk.shit.objects.Object;
import com.idk.shit.objects.Player;
import com.idk.shit.ui.Button;
import com.idk.shit.ui.TextRenderer;
import com.idk.shit.utils.Colours;
import com.idk.shit.utils.InputManager;
import com.idk.shit.utils.ScoreManager;
import com.idk.shit.utils.rand;

public class Level1View extends ApplicationView{
    private Deque<Object> blocks = new ArrayDeque<>();
    public float screen_height=1000;
    public float screen_width=650;
    public float RATIO = (float)screen_width/(float)screen_height;
    public Player player;
    private int bestscore;
    private int score = 0;
    private Button redButton;
    private Texture playerTexture;
    private Texture blockTexture;
    private Texture background;
    private Level1 level1;
    public Level1View (State state, Level1 level1, long window, InputManager inputManager, long vg, TextRenderer textRenderer, ScoreManager scoreManager) {
        super( state, window, inputManager, vg, textRenderer, scoreManager); // Передаем window в родительский класс
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
            state.Menu();
            cleanup();
            inputManager.cleanup();
        }
        player = level1.getPlayer();
        if (player.fall_down()==true){
            state.GameOver();
            cleanup();
            inputManager.cleanup();
        }
    }
    public void render() {
        background.draw(0f, 0f, 2*RATIO, 2);
        blocks = level1.getBlocks();
        for (Object block : blocks) {
            block.draw(blockTexture);
        }
        redButton.draw();
        player = level1.getPlayer();
        player.draw(playerTexture);
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
