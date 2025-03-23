package com.idk.shit.game.views.view.Implementations.PlaysView;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

import com.idk.shit.graphics.Texture;
import com.idk.shit.graphics.TextureCache;
import com.idk.shit.game.state.State;
import com.idk.shit.game.views.view.ApplicationView;
import com.idk.shit.graphics.Shader;

import com.idk.shit.objects.Object;
import com.idk.shit.objects.Player;
import com.idk.shit.ui.Button;
import com.idk.shit.ui.TextRenderer;
import com.idk.shit.utils.Colours;
import com.idk.shit.utils.InputManager;
import com.idk.shit.utils.ScoreManager;
import com.idk.shit.utils.rand;

public class PlayingView extends ApplicationView {
    private Deque<Object> blocks = new ArrayDeque<>();
    private Deque<Object> supposed_blocks = new ArrayDeque<>();
    public float screen_height=1000;
    public float screen_width=650;
    public float RATIO = (float)screen_width/(float)screen_height;
    private float block_height = 0.045f;
    private float block_width = 0.25f;
    private Player player;
    private Object block;
 
    private float speed_player_x = 0.03f;
    private float max_speed_y = 0.07f;
    private float accel_y = -0.003f;
    private int score = 0;
    private float max_height = -max_speed_y * max_speed_y / (2 * accel_y) - max_speed_y;
    private Button redButton;
    private Texture playerTexture;
    private Texture blockTexture;

    // protected Shader shader = new Shader("vertex_shader.glsl", "fragment_shader.glsl");



    public PlayingView(State state, long window, InputManager inputManager, long vg, TextRenderer textRenderer, ScoreManager scoreManager) {
        super( state, window, inputManager, vg, textRenderer, scoreManager); // Передаем window в родительский класс
        redButton = new Button(-0.7f, 0.95f, 0.6f, 0.1f, "menu", Colours.GREEN, vg, textRenderer);
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
        playerTexture = TextureCache.getTexture("src\\main\\resources\\textures\\pngegg.png");
        blockTexture = TextureCache.getTexture("src\\main\\resources\\textures\\трава.png");
        // player = new Player(0.0f, 0.0f, 0.15f, 0.23f, 0.02f,Colours.WHITE, this.playerTexture,);
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
    public void  update() throws Exception {
        if (player.fall_down()==true){
            cleanup();
            state.GameOver();
            return;
        }
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
            block.collision(player);
        }

        redButton.update(this.window);
        if (redButton.isClicked()||inputManager.isKeyPressed(GLFW_KEY_SPACE)) {
            state.Menu();
            cleanup();
            inputManager.cleanup();
            return;
            
        }
    }
    @Override
    public void render() {
        for (Object block : blocks) {
            block.draw(blockTexture);
        }
        redButton.draw();
        player.draw(playerTexture);
    }
    @Override
    public void cleanup() {
        blocks.clear();
        supposed_blocks.clear();
        player = null;
        block = null;
        // redButton = null;
    }   
}