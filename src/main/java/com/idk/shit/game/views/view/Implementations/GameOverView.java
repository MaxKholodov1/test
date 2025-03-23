package com.idk.shit.game.views.view.Implementations;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

import com.idk.shit.game.state.ValueObjects.ApplicationState;
import com.idk.shit.game.state.ValueObjects.Implementations.GameOver;
import com.idk.shit.ui.Button;
import com.idk.shit.utils.Colours;
import com.idk.shit.utils.InputManager;
import com.idk.shit.game.state.State;
import com.idk.shit.game.views.view.ApplicationView;
import com.idk.shit.ui.TextRenderer;
import com.idk.shit.utils.ScoreManager;

public class GameOverView extends ApplicationView{
    private Button gameButton;
    protected int score;
    protected int level;
    private int bestscore;
    public GameOverView(State state,long window, InputManager inputManager, long vg, TextRenderer textRenderer, ScoreManager scoreManager) {
        super(state, window, inputManager, vg, textRenderer, scoreManager);
        gameButton = new Button(0.f, 0.f, 1f, 0.5f, "TRY AGAIN!", Colours.BROWN, vg, textRenderer);
        initGameOver();
    }
    private void initGameOver(){
    }
    @Override
    public void  update() throws Exception{
        gameButton.update(window);
        if (gameButton.isClicked() || inputManager.isKeyPressed(GLFW_KEY_SPACE))  {
            inputManager.cleanup();
            state.Play(level);
        }
    }
    @Override
    public void render(){
        gameButton.draw();
        score = ((GameOver)state.getAppState()).getScore();
        level = ((GameOver)state.getAppState()).getLevel();
        bestscore = ((GameOver)state.getAppState()).getBestscore();

        String scoreString = String.valueOf(score);
        String bestscoreString = String.valueOf(bestscore);

        textRenderer.drawText(0f, 0.9f, "your score:", Colours.BLACK, vg, 0.5f, 0.5f);
        textRenderer.drawText(-0.005f, 0.8f, scoreString, Colours.BLACK, vg, 0.2f, 0.2f);
        textRenderer.drawText(0f, 0.6f, "best score:", Colours.BLACK, vg, 0.5f, 0.5f);
        textRenderer.drawText(-0.005f, 0.5f, bestscoreString, Colours.BLACK, vg, 0.2f, 0.2f);
    }
}