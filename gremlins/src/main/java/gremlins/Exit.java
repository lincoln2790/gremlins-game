package gremlins;
import processing.core.PImage;
import processing.core.PApplet;
public class Exit extends Tile{
    public
        Exit(int x, int y, PImage Image){
            super(x, y);
            this.Image = Image;
            this.IsActive = true;
        }


        public void tick(){
            
        }
        public void draw(PApplet app){
            app.image(this.Image, this.x, this.y);
            
        }
};