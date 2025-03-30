package com.idk.shit.game.state.ValueObjects.objects;

public class Meteor extends Object {
    protected float speed_y=-0.025f;
    protected float accel_y=0f;
    private final float screen_width=650;
    private final float screen_height=1000;
    private final float RATIO=screen_width/screen_height;

    public Meteor(float startX, float startY, float width, float height, float speed, float[] colour) {
        super(startX, startY, width, height, speed, colour);
    }
    public boolean fall_down(){
        return this.y < -1;
    }
    @Override
    public void update_object(float speed) {
        speed_y+=accel_y;
        this.y+=speed_y;
        this.x+=speed;
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
