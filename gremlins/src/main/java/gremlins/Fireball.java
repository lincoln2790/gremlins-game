package gremlins;

import processing.core.PImage;

public class Fireball extends Entity {

    int VELOCITY = 5;
    PImage powerImage,bigBoomImage;
    Fireball(int x, int y, PImage Image,PImage powerImage){
        super(x,y);
        this.Image = Image;
        this.powerImage = powerImage;
        this.bigBoomImage = powerImage;
        this.IsAlive = false;
        this.type = 'F';
        
    }
    void tick(boolean CheckCollision){
        if(!CheckCollision){
            move();
        } else {
            IsAlive = false;
        }
        
    }

}
