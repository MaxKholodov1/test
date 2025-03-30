package com.idk.shit.game.views.view.Implementations.PlaysView;

import com.idk.shit.game.state.StateManager;
import com.idk.shit.game.views.view.ApplicationView;
import com.idk.shit.ui.TextRenderer;
import com.idk.shit.utils.InputManager;
import com.idk.shit.utils.ScoreManager;

public class PlayingView extends ApplicationView {
    public PlayingView(StateManager stateManager, long window, InputManager inputManager, long vg, TextRenderer textRenderer, ScoreManager scoreManager) {
        super(stateManager, window, inputManager, vg, textRenderer, scoreManager); // Передаем window в родительский класс
    }

    private void initGame() {}
    
    @Override
    public void  update() throws Exception {
    }
    @Override
    public void render() {}
    @Override
    public void cleanup() {
    }   
}