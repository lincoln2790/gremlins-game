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
        public void TargetTile(int key){
            int[] target = {x,y};
            if (key == 37) { //left arrow
                Image = Sequence.get(0);
                // for(int i = 0;i<20;i++)
                target[0] -=20;
                    // x--;
            } else if (key == 39) { //right arrow
                Image = Sequence.get(1);
                // for(int i = 0;i<20;i++)
                target[0] +=20;
                    // x++;
            } else if (key == 38) { //a
                Image = Sequence.get(2);
                // for(int i = 0;i<20;i++)
                target[1] -=20;
                    // y--;
            } else if (key == 40) { //d
                Image = Sequence.get(3);
                // for(int i = 0;i<20;i++)
                target[1] +=20;
                    // y++;
            }
            this.target = target;
            // return target;

        }
        // public void move(int dx,int dy){
        //     for(int i = 0; i<)
        //     x += dx;
        //     y += dy;
        // }
        public void tick(){
            if(!(target[0] == x && target[1] == y)){
                
                if(target[0]-x >0){
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

        }
        public void draw(PApplet app){
            // app.image(this.Sequence.get(4), this.x, this.y);
            
        }
};