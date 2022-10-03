package gremlins;

import processing.core.PImage;

public class Fireball extends Entity {
    int direction;
    int VELOCITY = 4;
    int Lifetime;
    Fireball(int x, int y, PImage Image){
        super(x,y);
        this.Image = Image;
        this.isAlive = false;
        this.Lifetime = 0;
        
    }
    void tick(){
        if(Lifetime == 0){
            isAlive = false;
        }
        Lifetime -= 1;
        System.out.println(Lifetime);
        move();
    }
    void move(){
        x += VELOCITY*h.get(direction).get(0);
        y += VELOCITY*h.get(direction).get(1);

    }
}
