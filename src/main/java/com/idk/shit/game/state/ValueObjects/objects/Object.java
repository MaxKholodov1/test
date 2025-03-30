package com.idk.shit.game.state.ValueObjects.objects;
public class Object  {
    protected float x,y;
    protected  float width, height; // Размеры игрока
    protected float speed;   // Скорость перемещения игрока
    protected float[] colour;
    // protected Shader shader;
    private final float screen_width=650;
    private final float screen_height=1000;
    private final float RATIO=screen_width/screen_height;
    // private Shader shader;

    public Object(float startX, float startY, float width, float height, float speed, float[] colour) {
        this.x = startX;
        this.y = startY;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.colour = colour;
    }
    public float getWidth(){
        return this.width;
    }
    public float getHeight(){
        return this.height;
    }
    public float getLeft() {
        return x - width / 2;
    }
    
    public float getRight() {
        return x + width / 2;
    }
    
    public float getBottom() {
        return y - height / 2;
    }
    
    public float getTop() {
        return y + height / 2;
    }
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
    public float getSpeed_x(){
        return this.speed;
    }
    public void SetY(float y){
        this.y=y;
    }
    public float height(){
        return this.height;
    }
    public float speed_y(){
        return 0f;
    }
    public void update_object(float speed) {
        // Обновление позиции игрока на основе нажатых клавиш
        this.x+=speed;
        if (this.x+width/2>=RATIO || this.x-width/2<=-RATIO){
            this.speed=-speed;
        }
    }
    public enum Direction {
        LEFT, 
        RIGHT, 
        UP, 
        DOWN,
        NONE
    }
    public String collision(Player a) {
        Direction dir = Direction.NONE;
        float dist=1f;
        float difx= -this.getSpeed_x()+a.getSpeed_x();
        float dify= a.speed_y()+a.accel_y();
        if (this instanceof Meteor){
            dify-=this.speed_y();
        }
        if (this.getRight()<a.getLeft() || this.getLeft()>a.getRight() ||  this.getBottom()>a.getTop()|| this.getTop()<a.getBottom()){
            if(!(a.getBottom()>this.getTop()||a.getTop()<this.getBottom())){
                if ((this.getRight()-a.getLeft())/difx>0f && (this.getRight()-a.getLeft())/difx<=dist){
                    dir = Direction.RIGHT; 
                    dist=(this.getRight()-a.getLeft())/difx;
                }
                if ((this.getLeft()-a.getRight()/difx)>0f && (this.getLeft()-a.getRight())/difx<=dist){
                    dir = Direction.LEFT; 
                    dist=(this.getLeft()-a.getRight())/difx;
                }
            }
            if (!(a.getRight()<this.getLeft()||a.getLeft()>this.getRight())) {
                if ((this.getTop()-a.getBottom())/dify>0f && (this.getTop()-a.getBottom())/dify<=dist){
                    dir = Direction.UP; 
                    dist=(this.getTop()-a.getBottom())/dify;
                }
                if ((this.getBottom()-a.getTop())/dify>0f && (this.getBottom()-a.getTop())/dify<=dist){
                    dir = Direction.DOWN; 
                    dist=(this.getBottom()-a.getTop())/dify;
                }
            }
        }
        boolean x_overlap = ! (this.getRight() < a.getLeft() || a.getRight() < this.getLeft());
        boolean y_overlap = !(this.getTop() < a.getBottom() || a.getTop() < this.getBottom());
        if (dir == Direction.UP) {
        
            return "UP";
        }
        if (dir == Direction.DOWN) {
      
            return "DOWN";
        }
        if (dir == Direction.LEFT) {
        
            return "LEFT";
        }
        if (dir == Direction.RIGHT) {
   
            return "RIGHT";
        }
        if (x_overlap && y_overlap){
            return "true";
        }
        dir = Direction.NONE;
        return "NONE";
    }
    
    
//    public void draw(Texture texture) {
//        texture.draw(this.x, this.y, this.width, this.height);
//    }
}
