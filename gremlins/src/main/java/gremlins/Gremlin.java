package gremlins;
import processing.core.PImage;

import java.util.ArrayList;


public class Gremlin extends Entity {
    public
        PImage Image;
        Gremlin(int x, int y, PImage Image){
            super(x, y);
            this.Image = Image;
        }

}