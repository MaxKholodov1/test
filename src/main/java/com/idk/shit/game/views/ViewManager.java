package com.idk.shit.game.views;

import com.idk.shit.game.state.State;
import com.idk.shit.game.state.ValueObjects.ApplicationState;
import com.idk.shit.game.state.ValueObjects.Implementations.GameOver;
import com.idk.shit.game.state.ValueObjects.Implementations.Plays.Levels.Level2;
import com.idk.shit.game.state.ValueObjects.Implementations.Plays.Play;
import com.idk.shit.game.state.ValueObjects.Implementations.Plays.Levels.Level1;
import com.idk.shit.game.state.ValueObjects.Implementations.Menu;
import com.idk.shit.game.views.view.ApplicationView;
import com.idk.shit.game.views.view.Implementations.GameOverView;
import com.idk.shit.game.views.view.Implementations.MenuView;
import com.idk.shit.game.views.view.Implementations.PlaysView.PlayingView;
import com.idk.shit.game.views.view.Implementations.PlaysView.LevelsView.*;
import com.idk.shit.ui.TextRenderer;

import com.idk.shit.utils.InputManager;
import com.idk.shit.utils.ScoreManager;

public class ViewManager {
    public State currentState;
    public ApplicationView currentView;
    public long window;
    public InputManager inputManager;
    public long vg;
    public TextRenderer textRenderer;
    public ScoreManager scoreManager;
    public ViewManager(long window, InputManager inputManager, State currentState, long vg,TextRenderer textRenderer, ScoreManager scoreManager ){
        this.window=window;
        this.inputManager=inputManager;
        this.currentState=currentState;
        this.vg = vg;
        this.textRenderer = textRenderer;
        this.scoreManager=scoreManager;
    }
    public void setState(ApplicationView newView){
        if(currentView!=null){
            currentView.cleanup(); // Освобождаем ресурсы предыдущего состояния
            currentView = null;
            currentView=newView;
            System.gc();
        }
        else{
            currentView = null;
            currentView=newView;
            newView = null;
            System.gc();
        }
    }
    public void update(){
        if(currentView!=null){
            if(currentView!=null){
                try {
                    currentView.update();  // Вызов метода, который может выбросить Exception
                } catch (Exception e) {
                    System.out.println("Обнаружена ошибка: " + e.getMessage());
                }
            }
        }
        UpdateViewByState();
    }
    public void render(){
        if(currentView!=null){
            currentView.render();
        }
    }

    private void UpdateViewByState(){
        ApplicationState state = currentState.GetApplicationState();
        if(state instanceof Menu && !(currentView instanceof MenuView)){
            currentView = new MenuView(currentState, window, inputManager, vg, textRenderer, scoreManager );
        }

        if(state instanceof GameOver && !(currentView instanceof GameOverView)){
            int score = currentView.GetScore();
            int level = currentView.GetLevel();
            currentView = new GameOverView(currentState,window, inputManager, vg, textRenderer, scoreManager);
        }
        if (state instanceof Play && !(currentView instanceof PlayingView)){
            if (state instanceof Level1  && !(currentView instanceof Level1View)){
                currentView=new Level1View(currentState, (Level1) state, window, inputManager, vg, textRenderer, scoreManager);
            }
            if (state instanceof Level2 && !(currentView instanceof Level2View)){
                currentView=new Level2View(currentState , (Level2) state, window, inputManager, vg, textRenderer, scoreManager );
            }
        }
    }
}