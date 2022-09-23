package gremlins;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.data.JSONArray;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Random;
import java.io.*;
import java.io.FileNotFoundException;
import processing.event.KeyEvent;

public class App extends PApplet {
    public static final int WIDTH = 720;
    public static final int HEIGHT = 720;
    public static final int SPRITESIZE = 20;
    public static final int BOTTOMBAR = 60;
    public static final int FPS = 60;
    public static final Random randomGenerator = new Random();
    public String configPath;

    ArrayList<Level> Levels = new ArrayList<Level>();
    ArrayList<ArrayList<Tile>> Tiles = new ArrayList< ArrayList<Tile> >();
    ArrayList< Fireball > Fireballs  = new ArrayList<Fireball >();
    Wizard Wizard;

    public App() {
        this.configPath = "config.json";
    }

//  Initialise the setting of the window size.
    public void settings() {
        size(WIDTH, HEIGHT);
    }


// Load all resources such as images. Initialise the elements such as the player, enemies and map elements.
    
    public void setup() {
        frameRate(FPS);
        // Load images during setup
        ArrayList< PImage> BrickwallImgs = new ArrayList<PImage>();
        String BrickwallImgStrs[] = {"brickwall.png", "brickwall_destroyed0.png","brickwall_destroyed1.png","brickwall_destroyed2.png","brickwall_destroyed3.png"};
        for(String s : BrickwallImgStrs){
            BrickwallImgs.add(loadImage(this.getClass().getResource(s).getPath().replace("%20", "")));
        }

        ArrayList< PImage> WizardImgs = new ArrayList<PImage>(); 
        String WizardImgStrs[] = {"wizard0.png","wizard1.png","wizard2.png","wizard3.png","fireball.png"};
        for(String s : WizardImgStrs){
            WizardImgs.add(loadImage(this.getClass().getResource(s).getPath().replace("%20", "")));
        }

        PImage StonewallImg = loadImage(this.getClass().getResource("stonewall.png").getPath().replace("%20", ""));
        PImage GremlinImg = loadImage(this.getClass().getResource("gremlin.png").getPath().replace("%20", ""));
        PImage ExitImg = loadImage(this.getClass().getResource("slime.png").getPath().replace("%20", ""));
        PImage FireballImg = loadImage(this.getClass().getResource("fireball.png").getPath().replace("%20", ""));
        
        for(int i = 0; i<5;i++){
            Fireballs.add(new Fireball(0, 0, ' ', FireballImg));
        }
        // File f = new File(Levels.get(0).Layout);
        File f = new File("level1.txt");
        try{
            Scanner scan = new Scanner(f);
            for(int row = 0; row<33;row++){
                String line = scan.nextLine();
                Tiles.add(new ArrayList<Tile>());
                for(int col = 0; col<36;col++){
                    Character c = line.charAt(col);
                    Tile t;
                    switch(c){
                        case 'X':
                            t = new StoneWall(col*20,row*20,StonewallImg);
                            break;
                        case 'B':
                            t = new BrickWall(col*20,row*20, BrickwallImgs);
                            break;
                        case 'E':
                            t = new Exit(col*20,row*20,ExitImg);
                            break;
                        case 'W':
                            Wizard = new Wizard(col*20,row*20, WizardImgs);
                        default:
                            t = new Tile(col*20,row*20);
                        

                    }
                    Tiles.get(row).add(t);
                }
            }

            scan.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        
        JSONObject conf = loadJSONObject(new File(this.configPath));
        // ArrayList<String> levelStrings = new ArrayList<String>();
        
        for(int i = 0; i< conf.getJSONArray("levels").size();i++){
            
            JSONObject level = conf.getJSONArray("levels").getJSONObject(i);
            String levelStr = level.getString("layout");
            Float levelWizardCooldown = level.getFloat("wizard_cooldown");
            Float  levelEnemyCooldown = level.getFloat("enemy_cooldown");
            Level levelObj = new Level(levelStr,levelWizardCooldown, levelEnemyCooldown);
            Levels.add(levelObj);
        }

    }
        // Object j = conf.getJSONArray("levels").get(0);
        // System.out.println(j.getClass());
        // JSONObject j = conf.getJSONArray("levels").get(0);
        // System.out.println(j.);
        


    

  

    /**
     * Receive key released signal from the keyboard.
    */
    public void keyReleased(){

    }


    /**
     * Draw all elements in the game by current frame. 
	 */
    public void draw() {
        this.background(192,156,124);
        this.Wizard.tick();
        
        this.image(Wizard.Image, Wizard.x, Wizard.y);
 
        for(Fireball f: Fireballs){
            if(f.IsActive){
                CheckFireballCollision(f);
                f.tick();
                this.image(f.Image, f.x,f.y);
            }
            
        }

        
        // System.out.printf();
        
        for(int row = 0; row<33;row++){
            for(int col = 0; col<36;col++){
                Tile t = Tiles.get(row).get(col);
                t.tick();
                // if(t.IsActive){
                if(( t.Image != null & t.TileType == 'B') | (t.TileType != 'B' & t.IsActive)){
                    this.image(t.Image, t.x, t.y);
                } else if (t.TileType == 'B' & t.IsActive){
                    this.image(t.Image, t.x, t.y);
                }
                    
            }
        }
    }

    // Key event triggered when any key is pressed
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
  
        if(key == 37) {
            Wizard.FacingDirection = 37;
            Wizard.xVel = -Wizard.Speed;
        }
        if(key == 38) {
            Wizard.FacingDirection = 38;
            Wizard.yVel = -Wizard.Speed;
        }
        if(key == 39) {
            Wizard.FacingDirection = 39;
            Wizard.xVel = Wizard.Speed;
        }
        if(key == 40) {
            Wizard.FacingDirection = 40;
            Wizard.yVel = Wizard.Speed;
        }

        if(key == 32){
            // int i = 0;
            for(int i = 0; i<5;i++){
                if(!Fireballs.get(i).IsActive){
                    Fireballs.get(i).IsActive = true;
                    Fireballs.get(i).x = Wizard.x;
                    Fireballs.get(i).y = Wizard.y;
                    Fireballs.get(i).direction = Wizard.FacingDirection;
                    Fireballs.get(i).Lifetime = 200;
                    break;
                }
            }

            // Fireballs.add(new Fireball(Wizard.x, Wizard.y, Wizard.FacingDirection, Wizard.Sequence.get(4)));
        }
        if(Wizard.target[0] == Wizard.x && Wizard.target[1] == Wizard.y){
            Wizard.TargetTile(e.getKeyCode(), Tiles);
        }
    }

    void CheckFireballCollision(Fireball f){
        int col = f.x/20;
        int row = f.y/20;
        if(f.x %20 == 0 && f.y%20 == 0){
            if(f.direction == 38){
                Tile AboveTile = Tiles.get(row-1).get(col);
                if(AboveTile.IsActive && AboveTile.TileType == 'B'){
                    Tiles.get(row-1).get(col).IsActive= false;
                    Tiles.get(row-1).get(col).AnimationIndex++;
                    f.IsActive = false;
                } else if (AboveTile.IsActive && AboveTile.TileType == 'S'){
                    f.IsActive = false;
                }
            }
            if(f.direction == 40){
                Tile AboveTile = Tiles.get(row+1).get(col);
                if(AboveTile.IsActive && AboveTile.TileType == 'B'){
                    Tiles.get(row+1).get(col).IsActive= false;
                    Tiles.get(row+1).get(col).AnimationIndex++;
                    f.IsActive = false;
                } else if (AboveTile.IsActive && AboveTile.TileType == 'S'){
                    f.IsActive = false;
                }
            }
            if(f.direction == 39){
                Tile AboveTile = Tiles.get(row).get(col+1);
                if(AboveTile.IsActive && AboveTile.TileType == 'B'){
                    Tiles.get(row).get(col+1).IsActive= false;
                    Tiles.get(row).get(col+1).AnimationIndex++;
                    f.IsActive = false;
                } else if (AboveTile.IsActive && AboveTile.TileType == 'S'){
                    f.IsActive = false;
                }
            }
            if(f.direction == 37){
                Tile AboveTile = Tiles.get(row).get(col-1);
                if(AboveTile.IsActive && AboveTile.TileType == 'B'){
                    Tiles.get(row).get(col-1).IsActive= false;
                    Tiles.get(row).get(col-1).AnimationIndex++;
                    f.IsActive = false;
                } else if (AboveTile.IsActive && AboveTile.TileType == 'S'){
                    f.IsActive = false;
                }
            }

        }
    }
    public static void main(String[] args) {
        PApplet.main("gremlins.App");
    }
}

