package com.idk.shit.game.views.view;
import com.idk.shit.game.state.StateManager;
import com.idk.shit.ui.TextRenderer;
import com.idk.shit.utils.InputManager;
import com.idk.shit.utils.ScoreManager;

public abstract  class ApplicationView {
    protected long window;
    protected InputManager inputManager;
    protected StateManager stateManager;
    protected long vg;
    protected TextRenderer textRenderer;
    protected ScoreManager scoreManager;
    public ApplicationView(StateManager stateManager, long window, InputManager inputManager, long vg, TextRenderer textRenderer, ScoreManager scoreManager ){
        this.stateManager = stateManager;
        this.window=window;
        this.inputManager=inputManager;
        this.vg = vg;
        this.textRenderer=textRenderer;
        this.scoreManager=scoreManager;
    }
    public abstract void update() throws Exception;
    public abstract void render();
    // public abstract void cleanup();

    public void cleanup() {

    }
    public int GetScore() {
        return 0;
    }
    public int GetLevel() {
        return 0;
    }
}