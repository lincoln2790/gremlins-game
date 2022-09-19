package gremlins;
import processing.core.PImage;

import java.util.ArrayList;


import processing.core.PApplet;
public class Wizard extends Entity{
    public
        // Sequence
   
        ArrayList< PImage > Sequence;
        int AnimationIndex;
        // Pimage image;
        Wizard(int x, int y, ArrayList< PImage > Sequence){
            super(x, y);
            this.Sequence = Sequence;
            this.Image = Sequence.get(0);
            this.AnimationIndex = 0;
        }
        public void left(){
            Image = Sequence.get(0);
            x -= 20;
        }
        public void right(){
            Image = Sequence.get(1);
            x += 20;
        }
        public void down(){
            Image = Sequence.get(3);
            y += 20;
        }
        public void up(){
            Image = Sequence.get(2);
            y -= 20;
        }
        public void tick(){
            
        }
        public void draw(PApplet app){
            // app.image(this.Sequence.get(4), this.x, this.y);
            
        }
};