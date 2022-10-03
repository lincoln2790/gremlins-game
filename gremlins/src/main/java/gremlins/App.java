package gremlins;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.data.JSONArray;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
import java.io.*;
import java.io.FileNotFoundException;
import processing.event.KeyEvent;

public class App extends PApplet {
    // Defining constants
    public static final int WIDTH = 720;
    public static final int HEIGHT = 720;
    public static final int SPRITESIZE = 20;
    public static final int BOTTOMBAR = 60;
    public static final int FPS = 60;
    public static final int NUM_ROWS = 33;
    public static final int NUM_COLS = 36;
    public int keyCode;
    public int keysPressed[] = {0,0,0,0};

    public static final Random randomGenerator = new Random();
    public String configPath;

    ArrayList<Level> Levels = new ArrayList<Level>();
    int currentLevel = 0;
    

    public App() {
        this.configPath = "config.json";
    }

    public void settings() {
        size(WIDTH, HEIGHT);
    }

    public void setup() {
        frameRate(FPS);
        // Loading BrickWall animation images
        ArrayList< PImage> BrickwallImgs = new ArrayList<PImage>();
        String BrickwallImgStrs[] = {"brickwall.png", "brickwall_destroyed0.png","brickwall_destroyed1.png","brickwall_destroyed2.png","brickwall_destroyed3.png"};
        for(String s : BrickwallImgStrs){
            BrickwallImgs.add(loadImage(this.getClass().getResource(s).getPath().replace("%20", "")));
        }

        // Loading Wizard animation images
        ArrayList< PImage> WizardImgs = new ArrayList<PImage>(); 
        String WizardImgStrs[] = {"wizard0.png","wizard2.png","wizard1.png","wizard3.png","fireball.png"};
        for(String s : WizardImgStrs){
            WizardImgs.add(loadImage(this.getClass().getResource(s).getPath().replace("%20", "")));
        }

        // Loading all other images
        PImage StonewallImg = loadImage(this.getClass().getResource("stonewall.png").getPath().replace("%20", ""));
        PImage GremlinImg = loadImage(this.getClass().getResource("gremlin.png").getPath().replace("%20", ""));
        PImage ExitImg = loadImage(this.getClass().getResource("slime.png").getPath().replace("%20", ""));
        PImage FireballImg = loadImage(this.getClass().getResource("fireball.png").getPath().replace("%20", ""));
        
        
        JSONObject conf = loadJSONObject(new File(this.configPath));
 
        for(int i = 0; i< conf.getJSONArray("levels").size();i++){
            JSONObject level = conf.getJSONArray("levels").getJSONObject(i);
            String levelStr = level.getString("layout");
            Float levelWizardCooldown = level.getFloat("wizard_cooldown");
            Float  levelEnemyCooldown = level.getFloat("enemy_cooldown");
            Level levelObj = new Level(levelStr,levelWizardCooldown, levelEnemyCooldown);
            for(int f = 0; f< Level.MAX_FIREBALLS;f++){
                levelObj.Fireballs.add(new Fireball(0, 0, FireballImg));
            }
            File f = new File(levelStr);
            Levels.add(levelObj);
            try{
                Scanner scan = new Scanner(f);
                for(int row = 0; row<NUM_ROWS;row++){
                    String line = scan.nextLine();
                    
                    Levels.get(i).Tiles.add(new ArrayList<Tile>());
                    for(int col = 0; col<NUM_COLS;col++){
                        Character c = line.charAt(col);
                        Tile t =new Tile(col*20,row*20);
                        Level l = Levels.get(currentLevel);
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
                                Wizard w = new Wizard(col*20,row*20, WizardImgs, l.WizardCooldown);
                                Levels.get(i).Wizard = w;
                                break;
                            case 'G':
                                Gremlin g = new Gremlin(col*20,row*20, GremlinImg, l.EnemyCooldown);
                                Levels.get(i).Gremlins.add(g);
                                break;
                        }
                        Levels.get(i).Tiles.get(row).add(t);
 
                    }
                }
                scan.close();
            }
            catch(FileNotFoundException e){
                e.printStackTrace();
            }
        }
        

    }

    public void draw() {

        this.background(192,156,124);
        Level l = Levels.get(currentLevel);

        
        System.out.printf("%d \n",l.Wizard.cooldown);

        this.image(l.Wizard.Image, l.Wizard.x, l.Wizard.y);

        for(int row = 0; row<NUM_ROWS;row++){
            for(int col = 0; col<NUM_COLS;col++){
                Tile t = l.Tiles.get(row).get(col);
                t.tick();
                // if(t.IsActive){
                if(( t.Image != null & t.TileType == 'B') | (t.TileType != 'B' & t.IsActive)){
                    this.image(t.Image, t.x, t.y);
                } else if (t.TileType == 'B' & t.IsActive){
                    this.image(t.Image, t.x, t.y);
                }
                    
            }
        }
        for(Gremlin g: l.Gremlins){
            if(g.isAlive){
                this.image(g.Image,g.x, g.y);
                g.tick(CheckEntityCollision(g));
            }
            

        }
        for(Fireball f: l.Fireballs){
            if(f.isAlive){
                this.image(f.Image, f.x, f.y);
                
                for(int i = 0; i<f.VELOCITY;i++){
                    CheckFireBallCollision();
                    f.tick();
                }
                
                
            }
            
        }
        l.Wizard.tick(CheckEntityCollision(l.Wizard));
    }    // Key event triggered when any key is pressed

    public void keyPressed(KeyEvent e) {
        keyCode = e.getKeyCode();
        
        Level l =  Levels.get(currentLevel);
        if(keyCode >=37 && keyCode<=40){
            keysPressed[keyCode-37] = 1;
            l.Wizard.ArrowKeyQueue = e.getKeyCode();
            l.Wizard.isKeyPressed = true;
            if(!l.Wizard.isMoving){
                l.Wizard.Direction = keyCode;
                
            }
        }
        if(l.Wizard.isGridLocked()){
            l.Wizard.Direction = l.Wizard.ArrowKeyQueue;
        }
        if(keyCode == 32){
            if(l.Wizard.cooldownTimer <= 0){
                for(int i = 0; i<5;i++){
                    if(!l.Fireballs.get(i).isAlive){
                        l.Fireballs.get(i).isAlive = true;
                        l.Fireballs.get(i).x = l.Wizard.x;
                        l.Fireballs.get(i).y = l.Wizard.y;
                        l.Fireballs.get(i).direction = l.Wizard.Direction;
                        l.Fireballs.get(i).Lifetime = 2000;
                        l.Wizard.cooldownTimer = l.Wizard.cooldown;
                        break;
                    }
                }
            }
            

        }
    }
    public void keyReleased(KeyEvent e){
        keyCode = e.getKeyCode();
        if(keyCode >=37 && keyCode<=40){
            keysPressed[keyCode-37] = 0;
        }
        int totalKeysPressed = 0;
        for(int i = 0;i<4;i++){
            totalKeysPressed+=keysPressed[i];
        }
        if(totalKeysPressed ==0){
            Levels.get(currentLevel).Wizard.isKeyPressed = false;
        }
        
        // if(keyCode == 32){

        // } else if(keyCode >=37 && keyCode<=40){
        //     Levels.get(currentLevel-1).Wizard.isKeyPressed = false;
        // }

    }
   
    public static void main(String[] args) {
        PApplet.main("gremlins.App");
    }
    public void CheckFireBallCollision(){
        Level l = Levels.get(currentLevel);
        boolean Collision = false;
        outerloop:
        for(int row = 0;row<NUM_ROWS;row++){
            for(int col = 0;col<NUM_COLS;col++){
                Tile t = l.Tiles.get(row).get(col);
                
                for(Fireball f:l.Fireballs){
                    if(f.isAlive && t.IsActive){
                        if(f.direction == LEFT && f.x == t.x+20 && f.y == t.y) Collision = true;
                        if(f.direction == UP && f.x == t.x && f.y == t.y+20) Collision = true;
                        if(f.direction == RIGHT && f.x == t.x-20 && f.y == t.y) Collision = true;
                        if(f.direction == DOWN && f.x == t.x && f.y == t.y-20) Collision = true;
                        if(f.x == t.x && f.y == t.y) Collision = true;

                        if(Collision){
                            switch(t.TileType){
                                case 'B': 
                                    f.isAlive = false;
                                    t.IsActive= false;
                                    break;
                                case 'S': 
                                    f.isAlive = false;
                                    // t.IsActive= false;
                                    break;

                            }
                            break outerloop;
                        }
                        
                            
                    }
                }
            }
        }
        
        Collision = false;
        outerloop:
        for(Gremlin g: l.Gremlins){
            for(Fireball f:l.Fireballs){
                if(f.direction == LEFT && f.x == g.x+20 && f.y == g.y) Collision = true;
                if(f.direction == UP && f.x == g.x && f.y == g.y+20) Collision = true;
                if(f.direction == RIGHT && f.x == g.x-20 && f.y == g.y) Collision = true;
                if(f.direction == DOWN && f.x == g.x && f.y == g.y-20) Collision = true;
                if(f.x == g.x && f.y == g.y) Collision = true;
                if(Collision){
                    g.isAlive = false;
                    f.isAlive= false;
                    break outerloop;
                }
            }
            
        }
        // return Collision;
        
    }
    boolean CheckEntityCollision(Entity e){
        Level l = Levels.get(currentLevel);
        boolean Collision = false;
        if(e.isGridLocked()){
            outerloop:
            for(int row = 0;row<NUM_ROWS;row++){
                for(int col = 0;col<NUM_COLS;col++){
                    Tile t = l.Tiles.get(row).get(col);
                    if(t.IsActive){
                        if(e.Direction == LEFT && e.x == t.x+20 && e.y == t.y) Collision = true;
                        if(e.Direction  == UP && e.x == t.x && e.y == t.y+20) Collision = true;
                        if(e.Direction  == RIGHT && e.x == t.x-20 && e.y == t.y) Collision = true;
                        if(e.Direction  == DOWN && e.x == t.x && e.y == t.y-20) Collision = true;
                    
                    }
                    if(Collision){
                        // l.Wizard.isMoving = false;
                        break outerloop;
                    }
                }
            }
        }
        
        
        return Collision;

    }
    
}

