package gremlins;
import java.util.ArrayList;

import processing.core.PImage;
class Level{
    public
        String Layout;
        float WizardCooldown;
        float EnemyCooldown;
        Wizard Wizard;
        // ArrayList< Fireball > Fireballs  = new ArrayList<Fireball >();
        ArrayList<ArrayList<Tile>> Tiles = new ArrayList< ArrayList<Tile> >();
        ArrayList<Gremlin> Gremlins = new ArrayList<Gremlin  >();
        Level(String Layout, float WizardCooldown, float EnemyCooldown){
            this.Layout= Layout;
            this.WizardCooldown= WizardCooldown;
            this.EnemyCooldown = EnemyCooldown;
        }
};