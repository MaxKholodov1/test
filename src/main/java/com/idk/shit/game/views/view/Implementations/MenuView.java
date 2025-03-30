package com.idk.shit.game.views.view.Implementations;


import static org.lwjgl.glfw.GLFW.*;

import com.idk.shit.ui.Button;
import com.idk.shit.utils.Colours;
import com.idk.shit.utils.InputManager;
import com.idk.shit.game.state.StateManager;
import com.idk.shit.game.views.view.ApplicationView;
import com.idk.shit.ui.TextRenderer;
import com.idk.shit.utils.ScoreManager;


public class MenuView extends ApplicationView {
   
    private final Button Buttonlevel1;
    private final Button Buttonlevel2;
    Button[] buttons;
    public MenuView(StateManager stateManager, long window, InputManager inputManager, long vg, TextRenderer textRenderer, ScoreManager scoreManager) {
        super(stateManager, window, inputManager, vg, textRenderer, scoreManager);
        Buttonlevel1 = new Button(0.f, 0.5f, 1f, 0.6f, "LEVEL 1",Colours.BROWN, vg, textRenderer);
        Buttonlevel2 = new Button(0.f, -0.5f, 1f, 0.6f, "LEVEL 2",Colours.BROWN, vg, textRenderer);
        buttons = new Button[]{Buttonlevel1, Buttonlevel2};
        initMenu();
    }
    private void initMenu(){
        for (Button button : buttons) {
            button.draw();
        }
    }
    @Override
    public void update() throws Exception{
        for (Button button : buttons) {
            button.update(window);
        }
        for (int i = 0; i < buttons.length; i++) {
            int key = GLFW_KEY_1 + i; // i = 1 -> GLFW_KEY_1, i = 2 -> GLFW_KEY_2
            if(buttons[i].isClicked() || inputManager.isKeyPressed(key)){
                stateManager.Play(i+1);
            }
        }
    }
    @Override
    public void render(){
        for (Button button : buttons) {
            button.draw();
        }
    }
}