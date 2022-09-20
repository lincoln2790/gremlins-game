package gremlins;
import processing.core.PImage;

import java.util.ArrayList;


import processing.core.PApplet;
public class Fireball extends Entity{
    public
    int Velocity = 4;
    char direction;
    boolean IsActive;
    // int Lifetime;
    Fireball(int x, int y, char direction, PImage image){
        super(x,y);
        this.Image = image;
        this.direction = direction;
        this.IsActive = false;
        // this.Lifetime = 0;
    }
    void tick(){
        
            // if(direction == 'U'){
        switch(direction){
            case 'U':
            y-= Velocity;
            break;
            case 'D':
            y += Velocity;
            break;
            case 'L':
            x -= Velocity;
            break;
            case 'R':
            x += Velocity;
            break;
        }
        if(x %20 == 0 && y%20 == 0){    
            

        }
        // Sequence
    }
}
