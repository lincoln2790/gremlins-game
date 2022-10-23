package gremlins;
import processing.core.PImage;
import java.util.ArrayList;
import java.util.Random;


public class Gremlin extends Entity {
    public
        Random rand = new Random();
        ArrayList< Slimeball > Slimeballs  = new ArrayList<Slimeball >();

        Gremlin(int x, int y, PImage Image, float cooldown){
            super(x, y);
            this.Image = Image;
            this.Direction = rand.nextInt(4)+37;
            this.IsAlive = true;
            this.VELOCITY=1;
            this.type = 'G';
            this.cooldown = (int)cooldown*60;
        }

        public void tick(boolean CheckCollision){
            if(!CheckCollision){
                move();
            } else {
                Direction = rand.nextInt(4)+37;
            }
        }

}