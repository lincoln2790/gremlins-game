package gremlins;
import processing.core.PImage;
import processing.core.PApplet;

public class Power extends Entity{
    public
        Power(int x, int y, PImage Image){
            super(x, y);
            this.Image = Image;
            this.IsAlive = true;
            this.type = 'P';
        }


        public void tick(){
            
        }
        public void draw(PApplet app){
            app.image(this.Image, this.x, this.y);
            
        }
};