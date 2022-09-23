package gremlins;
import processing.core.PImage;

import java.util.ArrayList;


import processing.core.PApplet;
public class Wizard extends Entity{
    public
        // Sequence
   
        ArrayList< PImage > Sequence;
        
        int AnimationIndex;
        int key;
        int Speed = 5;
        int xVel;
        int yVel;
        int FacingDirection;
        int[] target= {0,0};


        // Pimage image;
        Wizard(int x, int y, ArrayList< PImage > Sequence){
            super(x, y);
            this.Sequence = Sequence;
            this.Image = Sequence.get(0);
            this.AnimationIndex = 0;
            this.target[0] = x;
            this.target[1] = y;
            this.xVel = 0;
            this.yVel = 0;
        }

        public void TargetTile(int key, ArrayList<ArrayList<Tile>> Tiles){
            int[] target = {x,y};
            int targetRow = x/20;
            int targetCol = y/20;

            if (key == 37 && !Tiles.get(targetCol).get(targetRow-1).IsActive) { //left arrow
                Image = Sequence.get(0);
                FacingDirection = 37;
                target[0] -=20;
            } else if (key == 39 && !Tiles.get(targetCol).get(targetRow+1).IsActive) { //right arrow
                Image = Sequence.get(1);
                FacingDirection = 39;
                target[0] +=20;
            } else if (key == 38 && !Tiles.get(targetCol-1).get(targetRow).IsActive) { //a
                Image = Sequence.get(2);
                FacingDirection = 38;
                target[1] -=20;
            } else if (key == 40 && !Tiles.get(targetCol+1).get(targetRow).IsActive) { //d
                Image = Sequence.get(3);
                FacingDirection = 40;
                target[1] +=20;
            }
            this.target = target;
        }

        public void tick(){
            if(!(target[0] == x && target[1] == y)){
                if(FacingDirection == 37 || FacingDirection == 39) x += xVel;
                if(FacingDirection == 38 || FacingDirection == 40) y += yVel;

            }//
            


        }
        public void draw(PApplet app){
            // app.image(this.Sequence.get(4), this.x, this.y);
            
        }
        
};