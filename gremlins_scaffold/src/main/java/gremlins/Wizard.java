package gremlins;
import processing.core.PImage;

import java.nio.file.DirectoryIteratorException;
import java.util.ArrayList;


import processing.core.PApplet;
public class Wizard extends Entity{
    public
        // Sequence
   
        ArrayList< PImage > Sequence;
        
        int AnimationIndex;
        int key;
        int Speed = 5;
        char FacingDirection;
        int[] target= {0,0};


        // Pimage image;
        Wizard(int x, int y, ArrayList< PImage > Sequence){
            super(x, y);
            this.Sequence = Sequence;
            this.Image = Sequence.get(0);
            this.AnimationIndex = 0;
            this.target[0] = x;
            this.target[1] = y;
        }
        // public void left(){
        //     Image = Sequence.get(0);
        //     for(int i = 0;i<20;i++)
        //         x--;
        // }
        // public void right(){
        //     Image = Sequence.get(1);
        //     for(int i = 0;i<20;i++)
        //         x++;
        // }
        // public void down(){
        //     Image = Sequence.get(3);
        //     for(int i = 0;i<20;i++)
        //         y++;
        // }
        // public void up(){
        //     Image = Sequence.get(2);
        //     for(int i = 0;i<20;i++)
        //         y--;
        // }
        public void CheckForCollision(ArrayList<ArrayList<Tile>> Tiles){
  
            int targetRow = x/20;
            int targetCol = y/20;
            
            if (FacingDirection == 'L') { //left arrow
                if(Tiles.get(targetCol-1).get(targetRow).IsActive)  stop();
                else target[0] = 20*(targetRow-1);
                
            } 
            if (FacingDirection == 'R') { //left arrow
                if(Tiles.get(targetCol+1).get(targetRow).IsActive)  stop();
                else target[0] = 20*(targetRow+1);
                
            } 
            if (FacingDirection == 'U') { //left arrow
                if(Tiles.get(targetCol).get(targetRow-1).IsActive)  stop();
                else target[1] = 20*(targetCol-1);
                
            } 
            if (FacingDirection == 'D') { //left arrow
                if(Tiles.get(targetCol+1).get(targetRow).IsActive)  stop();
                else target[1] = 20*(targetRow+1);
            } 
        }

        public void stop(){
            // target[0] = x;
            // target[1] = y;
        }
        public void tick(){

            if(target[0]-x > 0){
                x += Speed;
            }
            else if(target[0]-x <0){
                x -= Speed;
            }
            else if(target[1]-y >0){
                y += Speed;
            }
            else if(target[1]-y <0){
                y -= Speed;
            }

            


        }
        public void draw(PApplet app){
            // app.image(this.Sequence.get(4), this.x, this.y);
            
        }
        
};