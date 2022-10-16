package gremlins;

import processing.core.PImage;

public class Fireball extends Entity {

    int VELOCITY = 5;
    int Lifetime;
    
    Fireball(int x, int y, PImage Image){
        super(x,y);
        this.Image = Image;
        this.IsAlive = false;
        this.Lifetime = 0;
        this.Direction = 37;
        this.type = 'F';
        
    }
    void tick(boolean CheckCollision){
        if(!CheckCollision){
            move();
            if(Lifetime == 0){
                IsAlive = false;
            }
            Lifetime--; 
        } else {
            IsAlive = false;
        }
        
    }
    void move(){
        x += h.get(Direction).get(0);
        y += h.get(Direction).get(1);

    }
}
