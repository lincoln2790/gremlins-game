package gremlins;
import processing.core.PImage;

import java.util.ArrayList;


import processing.core.PApplet;
public class Fireball extends Entity{
    public
    int Velocity = 4;
    int direction;
    boolean IsActive;
    int Lifetime;
    Fireball(int x, int y, char direction, PImage image){
        super(x,y);
        this.Image = image;
        this.direction = direction;
        this.IsActive = false;
        this.Lifetime = 0;
    }
    void tick(){
        if(Lifetime == 0){
            IsActive = false;
        }
        Lifetime -= 1;
        switch(direction){
            case 38:
            y-= Velocity;
            break;
            case 40:
            y += Velocity;
            break;
            case 37:
            x -= Velocity;
            break;
            case 39:
            x += Velocity;
            break;
        }
        if(x %20 == 0 && y%20 == 0){    
            

        }
        // Sequence
    }
}
