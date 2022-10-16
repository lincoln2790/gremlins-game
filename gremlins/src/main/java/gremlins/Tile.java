package gremlins;
import processing.core.PImage;
import java.util.ArrayList;
public class Tile {
    public
        int x,y;
        PImage Image;
        boolean IsActive;
        boolean IsDestructible;
        char TileType;
        int AnimationIndex;
        
        ArrayList< PImage > Sequence;
        Tile(int x,int y){
            this.x = x;
            this.y = y;
            
            this.IsActive = false;
            this.IsDestructible = false;
            this.TileType = 'E';
            this.Sequence = null;
            this.Image = null;

        }
        
        public void tick(){

        }
        // public void draw(){

        // }
    
}
