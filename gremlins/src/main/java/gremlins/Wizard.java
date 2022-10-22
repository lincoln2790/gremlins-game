package gremlins;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
// import java.util.Map.entry; 


public class Wizard extends Entity {
    public
        ArrayList< PImage > Sequence;
        boolean isMoving;
        boolean isKeyPressed;
        int ArrowKeyQueue;
        char type = 'W';
        int Lives;
        

        Wizard(int x, int y, ArrayList<PImage> Sequence, float cooldown){
            super(x, y);
            this.Sequence = Sequence;
            this.Image = Sequence.get(0);
            this.ArrowKeyQueue = 39;
            this.Direction = 37;
            this.isMoving = false;
            this.isKeyPressed = false;
            this.IsAlive = true;
            this.cooldown = (int)(cooldown*100);
            this.cooldownTimer = 0;

        }
        void tick(boolean CheckWallCollision){
            cooldownTimer--;
            if(isGridLocked()){
                Image = Sequence.get(Direction-37);
                if(!CheckWallCollision){
                    if(isKeyPressed){
                        Direction = ArrowKeyQueue;
                        move();
                        isMoving = true;
                    }else {
                        isMoving = false;
                    }
                }
            } else {
                move();
                isMoving = true;
            }
        }
        
        boolean isGridLocked(){
            return (x%20 == 0 && y%20 == 0);
        }
        boolean ArrowKeyPressed(){
            return (Direction >=37 && Direction<=40);
        }

}
