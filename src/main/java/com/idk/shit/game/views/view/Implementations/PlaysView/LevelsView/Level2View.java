package com.idk.shit.game.views.view.Implementations.PlaysView.LevelsView;
import java.util.ArrayDeque;
import java.util.Deque;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import com.idk.shit.game.state.ValueObjects.Implementations.Plays.Levels.Level2;
import com.idk.shit.game.views.view.Implementations.PlaysView.PlayingView;
import com.idk.shit.graphics.Texture;
import com.idk.shit.graphics.TextureCache;
import com.idk.shit.game.state.StateManager;
import com.idk.shit.game.state.ValueObjects.objects.Meteor;
import com.idk.shit.game.state.ValueObjects.objects.Object;
import com.idk.shit.game.state.ValueObjects.objects.Player;
import com.idk.shit.game.state.ValueObjects.objects.PrevMeteor;
import com.idk.shit.ui.Button;
import com.idk.shit.ui.TextRenderer;
import com.idk.shit.utils.Colours;
import com.idk.shit.utils.InputManager;
import com.idk.shit.utils.ScoreManager;

public class Level2View extends PlayingView {
    private Deque<Object> blocks = new ArrayDeque<>();
    private Deque<Meteor> meteors = new ArrayDeque<>();
    private Deque<PrevMeteor> prevmeteors = new ArrayDeque<>();

    public float screen_height=1000;
    public float screen_width=650;
    public float RATIO = screen_width / screen_height;
    private Player player;

    private int score = 0;
    private final Button redButton;
    private Texture playerTexture;
    private Texture blockTexture;
    private Texture meteorTexture;
    private Texture prevmeteorTexture;
    private final Texture background;
    private int bestscore = 0;
    private final Level2 level2;
    public Level2View(StateManager stateManager, Level2 level2, long window, InputManager inputManager, long vg, TextRenderer textRenderer, ScoreManager scoreManager) {
        super(stateManager, window, inputManager, vg, textRenderer, scoreManager); // Передаем window в родительский класс
        this.level2 = level2;
        redButton = new Button(-0.45f, 0.9f, 0.4f, 0.1f, "menu", Colours.GREEN, vg, textRenderer);
        background = TextureCache.getTexture("image.png");
        initGame();
    }
    private void initGame() {
        playerTexture = TextureCache.getTexture("pngegg.png");
        blockTexture = TextureCache.getTexture("трава.png");
        meteorTexture = TextureCache.getTexture("pngwing.com (1).png");
        prevmeteorTexture = TextureCache.getTexture("pngwing.com (2).png");
        bestscore = scoreManager.getHighScore(2);
    }

    @Override
    public void  update() throws Exception {
        if (player.fall_down()){
            cleanup();
            stateManager.GameOver();
            return;
        }// поражение при падении

        redButton.update(this.window);
        if (redButton.isClicked()||inputManager.isKeyPressed(GLFW_KEY_SPACE)) {
            stateManager.Menu();
            cleanup();
            inputManager.cleanup();
        }// в меню при нажатии пробела
    }
    @Override
    public void render() {
        background.draw(0f, 0f, 2*RATIO, 2);
        blocks = level2.getBlocks();
        for (Object block : blocks) {
            blockTexture.draw(block.getX(), block.getY(),block.getWidth(), block.getHeight());
        }
        meteors = level2.getMeteors();
        for (Meteor meteor : meteors) {
            meteorTexture.draw(meteor.getX(), meteor.getY(), meteor.getWidth(), meteor.getHeight());
        }
        redButton.draw();
        player = level2.getPlayer();
        playerTexture.draw(player.getX(), player.getY(),player.getWidth(), player.getHeight());
        prevmeteors = level2.getPrevmeteors();
        for (PrevMeteor prevmeteor : prevmeteors) {
            prevmeteorTexture.draw(prevmeteor.getX(), prevmeteor.getY(), prevmeteor.getWidth(), prevmeteor.getHeight());
        }
        score = level2.GetScore();
        String scoreString = String.valueOf(score);
        String bestscoreString = String.valueOf(bestscore);
        textRenderer.drawText(RATIO-0.1f, 0.9f, scoreString, Colours.WHITE, vg, 0.5f, 0.5f);
        textRenderer.drawText(RATIO - 0.1f, 0.8f, bestscoreString, Colours.ORANGE, vg, 0.15f, 0.15f);
    }
    @Override
    public void cleanup() {
        level2.cleanup();
    }
    @Override
    public int GetLevel(){
        return 2;
    }
    @Override
    public int GetScore(){
        return score;
    }
}
