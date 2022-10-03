package gremlins;

import processing.core.PImage;

public class Fireball extends Entity {
    int direction;
    int VELOCITY = 8;
    int Lifetime;
    Fireball(int x, int y, PImage Image){
        super(x,y);
        this.Image = Image;
        this.isAlive = false;
        this.Lifetime = 0;
        this.direction = 39;
        
    }
    void tick(){
        if(Lifetime == 0){
            isAlive = false;
        }
        Lifetime--;
        move();
    }
    void move(){
        x += h.get(direction).get(0);
        y += h.get(direction).get(1);

    }
}
