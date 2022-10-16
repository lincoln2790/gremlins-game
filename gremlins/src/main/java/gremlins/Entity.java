package gremlins;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import processing.core.PImage;

public class Entity {
    public 
        static final int LEFT = 37;
        static final int UP = 38;
        static final int RIGHT = 39;
        static final int DOWN = 40;
        int x, y;
        int Direction;
        int cooldown, cooldownTimer;
        PImage Image;
        boolean IsAlive;
        char type;
        int startX, startY;
        
        Map<Integer, ArrayList<Integer>> h = new HashMap<Integer, ArrayList<Integer>>();
        Entity(int x,int y){
            this.x=x;
            this.y = y;
            this.startX = x;
            this.startY = y;    
            this.Image = null;
            this.IsAlive = false;
            ArrayList<Integer> L = new ArrayList<Integer>(Arrays.asList(-1,0,0));
            ArrayList<Integer> R = new ArrayList<Integer>(Arrays.asList(1,0,1));
            ArrayList<Integer> U = new ArrayList<Integer>(Arrays.asList(0,-1,2));
            ArrayList<Integer> D = new ArrayList<Integer>(Arrays.asList(0,1,3));
            h.put(LEFT,L);
            h.put(UP,U);
            h.put(RIGHT,R);
            h.put(DOWN,D);
        }
        boolean isGridLocked(){
            return (x%20 == 0 && y%20 == 0);
        }
}
