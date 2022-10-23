package gremlins;

import processing.core.PImage;

public class Slimeball extends Entity {
    int VELOCITY = 4;
    Slimeball(int x, int y, PImage Image){
        super(x,y);
        this.Image = Image;
        this.IsAlive = false;
        this.type = 'S';
        
    }
    void tick(boolean CheckCollision){
        if(!CheckCollision){
            move();
        } else {
            IsAlive = false;
        }
        
    }
}
