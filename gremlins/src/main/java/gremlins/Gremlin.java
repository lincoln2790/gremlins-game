package gremlins;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.Random;


public class Gremlin extends Entity {
    public
        PImage Image;
        int VELOCITY=1;
        Random rand = new Random();
        Gremlin(int x, int y, PImage Image, float cooldown){
            super(x, y);
            this.Image = Image;
            this.cooldown = (int)cooldown;
            this.Direction = rand.nextInt(4)+37;
            this.isAlive = true;
        }
        public void tick(boolean CheckCollision){
            if(!CheckCollision){
                move();
            } else {
                Direction = rand.nextInt(4)+37;
            }
        }
        void move(){
            if(Direction != 0){
                x += VELOCITY*h.get(Direction).get(0);
                y += VELOCITY*h.get(Direction).get(1);

            }
            
        }
}