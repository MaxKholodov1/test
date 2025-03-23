package com.idk.shit.game.state.ValueObjects.Implementations.Plays.Levels;
import com.idk.shit.game.state.ValueObjects.Implementations.Plays.Play;
import com.idk.shit.objects.Meteor;
import com.idk.shit.objects.Object;
import com.idk.shit.objects.Player;
import com.idk.shit.objects.PrevMeteor;
import com.idk.shit.utils.Colours;
import com.idk.shit.utils.InputManager;
import com.idk.shit.utils.rand;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;

public class Level2 extends Play{
    private Deque<com.idk.shit.objects.Object> blocks = new ArrayDeque<>();
    private Deque<com.idk.shit.objects.Object> supposed_blocks = new ArrayDeque<>();
    private Deque<Meteor> meteors = new ArrayDeque<>();
    private Deque<PrevMeteor> prevmeteors = new ArrayDeque<>();

    public float screen_height=1000;
    public float screen_width=650;
    public float RATIO = (float)screen_width/(float)screen_height;
    private float block_height = 0.045f;
    private float block_width = 0.25f;
    private Player player;
    private com.idk.shit.objects.Object block;
    private Meteor meteor;
    private PrevMeteor prevmeteor;

    private float speed_player_x = 0.035f;
    private float max_speed_y = 0.05f;
    private float accel_y = -0.002f;
    private int score = 0;
    private float max_height = -max_speed_y * max_speed_y / (2 * accel_y) - max_speed_y;
    private InputManager inputManager;
    public Level2(InputManager inputManager) {
        this.inputManager = inputManager;
        initGame();
    }

    public void AddBlock( float left, float right, float b, float a, int score){
        rand randomizer = new rand();
        float x = randomizer.rand_x(left, right);
        float y = (float) (Math.random() * (b - a) + a);
        int res = randomizer.rand(new int[]{1, 2}, new int[]{score, 100});
        int speed_dir= randomizer.rand(new int[]{-1, 1}, new int[]{1, 1});
        float cloudx;
        if (res == 1) {
            block = new com.idk.shit.objects.Object(x, y, block_width, block_height, 0.006f*speed_dir, Colours.CYAN);
        } else {
            block = new com.idk.shit.objects.Object(x, y, block_width, block_height, 0.0f, Colours.PURPLE);
        }
        blocks.addLast(block);
        supposed_blocks.addLast(block);
        int met = randomizer.rand(new int[]{1, 2}, new int[]{score, 50});
        if (met==1 && score>6){

            float xm = randomizer.rand_x(left, right);
            meteor =new Meteor(xm, 2.3f, 0.075f, 0.075f, 0, Colours.BLACK);
            meteors.addLast(meteor);
            cloudx= (float)randomizer.rand_x(xm-(float)(0.075/2), (float)(xm+0.075/2));
            prevmeteor = new PrevMeteor(cloudx, 0.9f, 0.5f, 0.16f, 0, Colours.BLACK);
            prevmeteors.addLast(prevmeteor);
        }
    }

    private void initGame() {
        player = new Player(0.0f, -0.5f, 0.15f, 0.23f, 0.02f,Colours.WHITE, max_speed_y, accel_y);
        float left =  block_width / 2 - RATIO;
        float right = - block_width / 2 + RATIO;
        AddBlock(0f, 0.001f, -0.8f, -1f, score);
        float prev_height;
        for (int i = 0; i < 6; ++i) {
            prev_height = supposed_blocks.getLast().getTop();
            float a = prev_height + max_height / 2;
            float b = prev_height + max_height;
            AddBlock(left, right, b, a, score);

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
        }// обновление позиции игрока при нажатии клавиш

        for (Meteor meteor : meteors) {
            meteor.update_object(0f);
        }//обновление метеоров

        for (PrevMeteor prevmeteor : prevmeteors) {
            prevmeteor.update_object(0f);
        }//обновление prevметеоров

        for (com.idk.shit.objects.Object block : supposed_blocks) {
            float block_speed_x = block.getSpeed_x();
            block.update_object(block_speed_x);
        }// обновление блоков

        if (player.getY() > -0.1f) {
            for (com.idk.shit.objects.Object block : supposed_blocks) {
                block.SetY(block.getY()-(player.getY()-(-0.1f)));
            }
            for (Meteor meteor : meteors) {
                meteor.SetY(meteor.getY()-(player.getY()-(-0.1f)));
            }
            player.SetY(-0.1f);//обновлении позиций для бесконечного цикла
        }

        for (Iterator<com.idk.shit.objects.Object> iterator = blocks.iterator(); iterator.hasNext(); ) {
            com.idk.shit.objects.Object block = iterator.next();
            if (block.getY() < -1) {
                iterator.remove();
            }
        }// удаление блоков если они ниже -1

        for (Iterator<Meteor> iterator = meteors.iterator(); iterator.hasNext(); ) {
            Meteor Meteor = iterator.next();
            if (Meteor.getY() < -1) {
                iterator.remove();
            }// удаление метеоров если они ниже -1
        }
        for (Iterator<PrevMeteor> iterator = prevmeteors.iterator(); iterator.hasNext(); ) {
            PrevMeteor PrevMeteor = iterator.next();
            if (PrevMeteor.getY() < -1) {
                iterator.remove();
            }// удаление prevметеоров если они ниже -1
        }

        int cnt = 0;
        for (Iterator<com.idk.shit.objects.Object> iterator = supposed_blocks.iterator(); iterator.hasNext(); ) {
            com.idk.shit.objects.Object block = iterator.next();
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
            AddBlock(left, right, b, a, score);
        }

        for (com.idk.shit.objects.Object block : blocks) {
            if ("UP"==block.collision(player)){
                player.change_speed();
                player.SetY(block.getTop()+player.height()/2);
            }
        }
        for (Meteor meteor : meteors) {
            if ("true"==meteor.collision(player)){
                player.SetY(-2f);
            }
        }
    }
    public void cleanup() {
        blocks.clear();
        supposed_blocks.clear();
        player = null;
        block = null;
    }
    @Override
    public int getLevel(){
        return 2;
    }
    @Override
    public int getScore(){
        return score;
    }
    public int GetScore(){
        return score;
    }
    public Deque<Object> getBlocks() {
        return blocks;
    }
    public Player getPlayer() {
        return player;
    }
    public Deque<Meteor> getMeteors() {
        return meteors;
    }
    public Deque<PrevMeteor> getPrevmeteors() {
        return prevmeteors;
    }
}
