package com.idk.shit.game.state;

import com.idk.shit.game.state.ValueObjects.ApplicationState;
import com.idk.shit.game.state.ValueObjects.Implementations.Menu;
import com.idk.shit.game.state.ValueObjects.Implementations.Plays.Levels.Level2;
import com.idk.shit.game.state.ValueObjects.Implementations.Plays.Play;
import com.idk.shit.game.state.ValueObjects.Implementations.Plays.Levels.Level1;
import com.idk.shit.game.state.ValueObjects.Implementations.GameOver;
import com.idk.shit.utils.InputManager;
import com.idk.shit.utils.ScoreManager;

public class State {
    private ApplicationState CurrentState;
    private InputManager inputManager;
    private ScoreManager scoreManager;
    public ApplicationState GetApplicationState(){
        return CurrentState;
    }

    public State(InputManager inputManager, ScoreManager scoreManager) {
        CurrentState = new Menu();
        this.scoreManager = scoreManager;
        this.inputManager = inputManager;
    }

    public void Play(int i) throws Exception{
        if((CurrentState instanceof Menu) ||
        (CurrentState instanceof GameOver)){
            if (i==1){
                CurrentState=new Level1(inputManager);
            }else if(i==2){
                CurrentState=new Level2(inputManager);
            }
        }else{
            throw new Exception();
        }
        
    }

    public void GameOver() throws Exception{
        if(CurrentState instanceof Play){
            int score = ((Play) CurrentState).getScore();
            int level = ((Play) CurrentState).getLevel();
            CurrentState = new GameOver(score, level, scoreManager);
        }else{
            throw new Exception();
        }
        
    }
    public void Menu() throws Exception{
        if(CurrentState instanceof Play){
            CurrentState = new Menu();
        }else{
            throw new Exception();
        }
        
    }
    public void update(){
        CurrentState.update();
    }
    public ApplicationState getAppState(){
        return CurrentState;
    }
}
