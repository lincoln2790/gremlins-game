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
        PImage Image;
        boolean isAlive;
        Map<Integer, ArrayList<Integer>> h = new HashMap<Integer, ArrayList<Integer>>();
        Entity(int x,int y){
            this.x=x;
            this.y = y;
            this.Image = null;
            this.isAlive = false;
            ArrayList<Integer> L = new ArrayList<Integer>(Arrays.asList(-1,0,0));
            ArrayList<Integer> R = new ArrayList<Integer>(Arrays.asList(1,0,1));
            ArrayList<Integer> U = new ArrayList<Integer>(Arrays.asList(0,-1,2));
            ArrayList<Integer> D = new ArrayList<Integer>(Arrays.asList(0,1,3));
            h.put(LEFT,L);
            h.put(UP,U);
            h.put(RIGHT,R);
            h.put(DOWN,D);
        }
}
