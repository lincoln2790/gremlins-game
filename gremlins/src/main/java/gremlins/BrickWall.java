package gremlins;
import processing.core.PImage;

import java.util.ArrayList;


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
            this.TileType = 'B';
        }
        
        @Override
        public void tick(){
            if(!IsActive && AnimationIndex < 20){
                Image = Sequence.get((int)AnimationIndex/4);
                AnimationIndex++;
            }
            else if(!IsActive && AnimationIndex >= 20){
                Image = null;
                
            } else if(IsActive){
                Image = Sequence.get(0);
            }
        }

};