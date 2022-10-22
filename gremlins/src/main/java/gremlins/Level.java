package gremlins;
import java.util.ArrayList;

import processing.core.PImage;
class Level{
    public
        static final int MAX_FIREBALLS = 40;
        String Layout;
        int EnemyCooldownTimer;
        Wizard Wizard;
        Entity Exit;
        Power Burger;
        int BurgerTimer = 60*10;
        int BlueFireMatrix[][] = {
                    {0,-2},
            {-1,-1},{0,-1},{1,-1},
        {-2,0},{-1,0},{0,0},{1,0},{2,0},
            {-1,1},{0,1},{1,1},
                    {0,2}
        };
        
        ArrayList< Fireball > Fireballs  = new ArrayList<Fireball >();
        ArrayList<ArrayList<Tile>> Tiles = new ArrayList< ArrayList<Tile> >();
        ArrayList<Gremlin> Gremlins = new ArrayList<Gremlin  >();

        Level(String Layout){
            this.Layout= Layout;
        }
};