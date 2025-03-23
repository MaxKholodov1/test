package com.idk.shit.game.state.ValueObjects.Implementations;

import com.idk.shit.game.state.ValueObjects.ApplicationState;
import com.idk.shit.utils.ScoreManager;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

public class GameOver extends ApplicationState {
    protected int score;
    protected int level;
    protected ScoreManager scoreManager;
    protected int bestscore;
    public GameOver( int score, int level,ScoreManager scoreManager ) {
        this.score = score;
        this.level=level;
        this.scoreManager = scoreManager;
        initGameOver();
        bestscore = scoreManager.getHighScore(level);
    }
    private void initGameOver(){
        scoreManager.updateScore(level, score); // Обновит рекорд для уровня 1, если 600 больше текущего
        System.out.println(scoreManager.getHighScore(level));
        bestscore=scoreManager.getHighScore(level);
    }
    public int getScore() {
        return score;
    }
    public int getLevel() {
        return level;
    }
    public int getBestscore() {
        return bestscore;
    }
}
