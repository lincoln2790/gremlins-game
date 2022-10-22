package gremlins;

import processing.core.PImage;

public class Fireball extends Entity {

    int VELOCITY = 5;
    
    Fireball(int x, int y, PImage Image){
        super(x,y);
        this.Image = Image;
        this.IsAlive = false;
        this.Direction = 37;
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
