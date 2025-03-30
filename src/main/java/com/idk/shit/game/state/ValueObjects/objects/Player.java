package com.idk.shit.game.state.ValueObjects.objects;
public class Player extends Object {
    protected float speed_y=-0.0f;
    protected float max_speed_y;
    protected float accel_y;
    private final float screen_width=650;
    private final float screen_height=1000;
    private final float RATIO=screen_width/screen_height;

    public Player(float startX, float startY, float width, float height, float speed, float[] colour, float max_speed_y, float accel_y) {
        super(startX, startY, width, height, speed, colour);
        this.max_speed_y=max_speed_y;
        this.accel_y=accel_y;
    }
    public boolean fall_down(){
        return this.y < -1;
    }
    @Override
    public void update_object(float speed) {
        speed_y+=accel_y;
        this.y+=speed_y;
        if(this.x<-RATIO){
            this.x=2*RATIO+this.x;
        }
        this.x+=speed;
        // if (this.y<-1){
        //     this.y=0.5f;
        // }
        if (this.x>RATIO){
            this.x=-2*RATIO+this.x;
        }
    }
    public void change_speed(){
        speed_y=max_speed_y;
    }
    public float speed_y(){
        return speed_y;
    }
    public float accel_y(){
        return accel_y ;
    }
    public float height(){
        return height ;
    }
}
