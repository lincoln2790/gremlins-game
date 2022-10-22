package gremlins;
import java.util.ArrayList;

import processing.core.PImage;
class Level{
    public
        static final int MAX_FIREBALLS = 5;
        String Layout;
        int EnemyCooldownTimer;
        Wizard Wizard;
        Entity Exit;

        
        ArrayList< Fireball > Fireballs  = new ArrayList<Fireball >();
        ArrayList<ArrayList<Tile>> Tiles = new ArrayList< ArrayList<Tile> >();
        ArrayList<Gremlin> Gremlins = new ArrayList<Gremlin  >();

        Level(String Layout){
            this.Layout= Layout;
        }
};