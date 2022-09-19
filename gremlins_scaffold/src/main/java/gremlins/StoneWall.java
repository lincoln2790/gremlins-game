package gremlins;
import processing.core.PImage;
import processing.core.PApplet;
public class StoneWall extends Tile{
    public
        
        StoneWall(int x, int y, PImage Image){
            super(x, y);
            this.Image = Image;
        }


        public void tick(){
            
        }
        public void draw(PApplet app){
            app.image(this.Image, this.x, this.y);
            
        }
};