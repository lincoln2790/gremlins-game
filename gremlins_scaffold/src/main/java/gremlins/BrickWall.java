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
            this.AnimationIndex = 0;
            this.Image = Sequence.get(0);
            this.IsActive = true;
            this.IsDestructible = true;
            this.TileType = 'B';
        }
        
        @Override
        public void tick(){
            if(!IsActive & AnimationIndex <20){
                Image = Sequence.get((int)AnimationIndex/4);
                AnimationIndex++;
            }
            else if(AnimationIndex >= 20){
                Image = null;
            }
            // if( AnimationIndex == -1){

            // } else if (AnimationIndex <20){
            //     Image = Sequence.get((int)AnimationIndex/4);
            //     AnimationIndex++;
            //     System.out.println("WOOWEN");
            // } else {
            //     IsActive = false;
            // }
            // // this.Image = Sequence.get(AnimationIndex);

        }
        public void draw(PApplet app){
            // app.image(this.Sequence.get(4), this.x, this.y);
            
        }

};