package com.idk.shit.game.state.ValueObjects.Implementations.Plays.Levels;
import com.idk.shit.game.state.ValueObjects.Implementations.Plays.Play;
import com.idk.shit.game.state.ValueObjects.objects.Object;
import com.idk.shit.game.state.ValueObjects.objects.Player;
import com.idk.shit.utils.Colours;
import com.idk.shit.utils.InputManager;
import com.idk.shit.utils.rand;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
public class Level1 extends Play{
    private final Deque<Object> blocks = new ArrayDeque<>();
    private final Deque<Object> supposed_blocks = new ArrayDeque<>();
    public float screen_height=1000;
    public float screen_width=650;
    public float RATIO = screen_width / screen_height;
    private final float block_height = 0.06f;
    private final float block_width = 0.29f;
    private Player player;
    private Object block;
    private int score = 0;
    private final float speed_player_x = 0.03f;
    private final float max_speed_y = 0.07f;
    private final float accel_y = -0.003f;
    private final float max_height = -max_speed_y * max_speed_y / (2 * accel_y) - max_speed_y;
    public InputManager inputManager;

    public Level1( InputManager inputManager) {
        this.inputManager = inputManager;
        initGame();

    }

    public void AddBlock( float left, float right, float b, float a){
        rand randomizer = new rand();
        float x = randomizer.rand_x(left, right);
        float y = (float) (Math.random() * (b - a) + a);
        int res = randomizer.rand(new int[]{1, 2}, new int[]{score, 100});
        int speed_dir= randomizer.rand(new int[]{-1, 1}, new int[]{1, 1});
        if (res == 1) {
            block = new Object(x, y, block_width, block_height, 0.006f*speed_dir, Colours.CYAN);
        } else {
            block = new Object(x, y, block_width, block_height, 0.0f, Colours.PURPLE);
        }
        blocks.addLast(block);
        supposed_blocks.addLast(block);
    }

    private void initGame() {
        player = new Player(0.0f, 0.0f, 0.15f, 0.23f, 0.02f,Colours.WHITE, max_speed_y, accel_y);
        block = new Object(0.0f, -0.5f, block_width, block_height, 0.0f, Colours.PURPLE);
        float left =  block_width / 2 - RATIO;
        float right = - block_width / 2 + RATIO;
        AddBlock(0f, 0.001f, -0.4f, -0.5f);
        float prev_height;
        for (int i = 0; i < 6; ++i) {
            prev_height = supposed_blocks.getLast().getTop();
            float a = prev_height + max_height / 2;
            float b = prev_height + max_height;
            AddBlock(left, right, b, a);

        }
    }
    @Override
    public void  update() {
        if (inputManager.isKeyPressed(GLFW_KEY_LEFT) && !inputManager.isKeyPressed(GLFW_KEY_RIGHT) ) {
            player.update_object(-speed_player_x);
        } else if (inputManager.isKeyPressed(GLFW_KEY_RIGHT) && !inputManager.isKeyPressed(GLFW_KEY_LEFT)) {
            player.update_object(speed_player_x);
        } else {
            player.update_object(0);
        }

        for (Object block : supposed_blocks) {
            float block_speed_x = block.getSpeed_x();
            block.update_object(block_speed_x);
        }

        if (player.getY() > 0) {
            for (Object block : supposed_blocks) {
                block.SetY(block.getY()-player.getY());
            }
            player.SetY(0f);
        }

        for (Iterator<Object> iterator = blocks.iterator(); iterator.hasNext(); ) {
            Object block = iterator.next();
            if (block.getY() < -1) {
                iterator.remove();
            }
        }

        int cnt = 0;
        for (Iterator<Object> iterator = supposed_blocks.iterator(); iterator.hasNext(); ) {
            Object block = iterator.next();
            if (block.getY() < -1) {
                cnt++;
                score++;
                iterator.remove();
            }
        }

        float prev_height;
        for (int i = 0; i < cnt; ++i) {
            prev_height = supposed_blocks.getLast().getY();
            float a = prev_height + max_height / 2;
            float b = prev_height + max_height;
            float left =  block_width / 2  - RATIO;
            float right = -block_width / 2 + RATIO;
            AddBlock(left, right, b, a);
        }

        for (Object block : blocks) {
            if ("UP" == block.collision(player)){
                player.change_speed();
                player.SetY(block.getTop()+player.height()/2);
            }
        }
    }
    public void cleanup() {
        blocks.clear();
        supposed_blocks.clear();
        player = null;
        block = null;
    }
    public Deque<Object> getBlocks() {
        return blocks;
    }
    public Player getPlayer() {
        return player;
    }
    @Override
    public int getScore() {
            return score;
    }
    @Override
    public int getLevel() {
        return 1;
    }
}
