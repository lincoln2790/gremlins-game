package gremlins;
import processing.core.PImage;

import java.util.ArrayList;

import javax.sound.midi.Sequence;

import processing.core.PApplet;
public class BrickWall extends Tile{
    public
        // Sequence
        // ArrayList< PImage > Sequence;
        BrickWall(int x, int y, PImage Image){
            super(x, y);
            this.Image = Image;
            // this.Sequence = Sequence;
        }


        public void tick(){
            
        }
        public void draw(PApplet app){
            app.image(this.Image, this.x, this.y);
            
        }
};