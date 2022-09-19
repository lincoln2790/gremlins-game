package gremlins;
import processing.core.PImage;

import java.util.ArrayList;

import javax.sound.midi.Sequence;

import processing.core.PApplet;
public class BrickWall extends Tile{
    public
        // Sequence
   
        ArrayList< PImage > Sequence;
        int AnimationIndex;
        // Pimage image;
        BrickWall(int x, int y, ArrayList< PImage > Sequence){
            super(x, y);
            this.Sequence = Sequence;
            this.Image = Sequence.get(0);
            this.AnimationIndex = 0;
        }


        public void tick(){
            
        }
        public void draw(PApplet app){
            // app.image(this.Sequence.get(4), this.x, this.y);
            
        }
};