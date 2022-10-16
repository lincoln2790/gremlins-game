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
    ArrayList< PImage> BrickwallImgs = new ArrayList<PImage>();

    public App() {
        this.configPath = "config.json";
    }

    public void settings() {
        size(WIDTH, HEIGHT);
    }

    public void setup() {
        frameRate(FPS);
        // Loading BrickWall animation images
        // ArrayList< PImage> BrickwallImgs = new ArrayList<PImage>();
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
        Level l = Levels.get(currentLevel);
        this.background(192,156,124);
        // this.text()
        textSize(17);
        fill(255,255,255);
        text("Lives:",10, (NUM_ROWS+1)*20+10);
        text(String.format("Level %d/2",currentLevel+1),20*8+10, (NUM_ROWS+1)*20+10);

        if(l.Wizard.cooldownTimer>0){
            
            
            fill(255,255,255);
            rect(20*30+10, (NUM_ROWS+1)*20+10,99,10);
            fill(0,0,0);
            rect(20*30+10, (NUM_ROWS+1)*20+10,99-l.Wizard.cooldownTimer*3,10);
        }
        
        
        
        for(int i = 0;i < l.Wizard.Lives;i++){
            this.image(l.Wizard.Sequence.get(2),(3+i)*20, (NUM_COLS-2)*20);
        }

        this.image(l.Wizard.Image, l.Wizard.x, l.Wizard.y);
        

        for(int row = 0; row<NUM_ROWS;row++){
            for(int col = 0; col<NUM_COLS;col++){
                Tile t = l.Tiles.get(row).get(col);

                
                t.tick();
                if(t.Image != null){
                    this.image(t.Image, t.x, t.y);
                }
                // if(( t.Image != null & t.TileType == 'B') | (t.TileType != 'B' & t.IsActive)){
                    
                // } else if (t.TileType == 'B' & t.IsActive && t.AnimationIndex <20 ){
                //     this.image(t.Image, t.x, t.y);
                // }    
                
            }
        }

        for(Fireball f: l.Fireballs){
            if(f.IsAlive){
                this.image(f.Image, f.x, f.y);
                
                for(int i = 0; i<f.VELOCITY;i++){
                    f.tick(CheckEntityWallCollision(f));
                    
                    

                }
                for(Gremlin g: l.Gremlins){
                    if(g.IsAlive && CheckEntitiesCollision(f, g)){
                        g.IsAlive = false;
                        f.IsAlive= false;
                    }   
                    
                }
            }   
        }    
        l.Wizard.tick(CheckEntityWallCollision(l.Wizard));
        for(Gremlin g: l.Gremlins){
            if(g.IsAlive){
                this.image(g.Image,g.x, g.y);
                g.tick(CheckEntityWallCollision(g));
                if(CheckEntitiesCollision(g, l.Wizard)){
                    l.Wizard.Lives--;
                    reset();
                }
                
            }  
        }
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
                    if(!l.Fireballs.get(i).IsAlive){
                        l.Fireballs.get(i).IsAlive = true;
                        l.Fireballs.get(i).x = l.Wizard.x;
                        l.Fireballs.get(i).y = l.Wizard.y;
                        l.Fireballs.get(i).Direction = l.Wizard.Direction;
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
    public void reset(){
        Level l = Levels.get(currentLevel);
        l.Wizard.x = l.Wizard.startX;
        l.Wizard.y = l.Wizard.startY;
        for(int row = 0; row<NUM_ROWS;row++){
            for(int col = 0; col<NUM_COLS;col++){
                if(l.Tiles.get(row).get(col).TileType == 'B'){ 

                    l.Tiles.get(row).get(col).IsActive = true;
                    l.Tiles.get(row).get(col).AnimationIndex = 0;
                    // l.Tiles.get(row).get(col).tick();
                }
            }
        }
        for(int i = 0; i < l.Gremlins.size();i++){
            l.Gremlins.get(i).IsAlive = true;
            l.Gremlins.get(i).x = l.Gremlins.get(i).startX;
            l.Gremlins.get(i).y = l.Gremlins.get(i).startY;
        }
        
    }
   
    public static void main(String[] args) {
        PApplet.main("gremlins.App");
    }
    // public boolean CheckEntitiesCollision(Entity  e1, Entity e2){
    //     return false;
    // }
    // public boolean CheckEntityWallCollision(Entity e1){return false;}
    public boolean CheckEntitiesCollision(Entity  e1, Entity e2){
        // Entity Entity case;
        boolean Collision = false;
        if(e1.x < e2.x + 20 &&
            e1.x + 20 > e2.x &&
            e1.y < e2.y + 20 &&
            20 + e1.y > e2.y){
                Collision = true; 
        }
        // 
        return Collision;
    }
    public boolean CheckEntityWallCollision(Entity e1){
        // Entity Tile case;
        Level l = Levels.get(currentLevel);
        boolean Collision = false;
        outerloop:
        for(int row = 0;row<NUM_ROWS;row++){
            for(int col = 0;col<NUM_COLS;col++){
                Tile t = l.Tiles.get(row).get(col);

                if(t.TileType != 'E' && t.IsActive){
                    if(e1.Direction == LEFT && e1.x == t.x+20 && e1.y == t.y) Collision = true;
                    if(e1.Direction == UP && e1.x == t.x && e1.y == t.y+20) Collision = true;
                    if(e1.Direction == RIGHT && e1.x == t.x-20 && e1.y == t.y) Collision = true;
                    if(e1.Direction == DOWN && e1.x == t.x && e1.y == t.y-20) Collision = true;

                }       

                if(Collision && e1.type == 'F'){
                    switch(t.TileType){
                        case 'B': 
                            e1.IsAlive = false;
                            t.IsActive= false;
                            break;
                        case 'S': 
                            e1.IsAlive = false;
                            // t.IsActive= false;
                            break;
                    }
                    break outerloop;
                    
                }
                
            }
                  
        }
    

        return Collision;
    }



    
    // public void CheckFireBallCollision(){

    // }
    // boolean CheckEntityCollision(Entity e){
    //     return false;
    // }
    // public void CheckFireBallCollision(){
    //     Level l = Levels.get(currentLevel);
    //     boolean Collision = false;
    //     outerloop:
    //     for(int row = 0;row<NUM_ROWS;row++){
    //         for(int col = 0;col<NUM_COLS;col++){
    //             Tile t = l.Tiles.get(row).get(col);
                
    //             for(Fireball f:l.Fireballs){
    //                 if(f.IsAlive && t.IsActive){
    //                     if(f.Direction == LEFT && f.x == t.x+20 && f.y == t.y) Collision = true;
    //                     if(f.Direction == UP && f.x == t.x && f.y == t.y+20) Collision = true;
    //                     if(f.Direction == RIGHT && f.x == t.x-20 && f.y == t.y) Collision = true;
    //                     if(f.Direction == DOWN && f.x == t.x && f.y == t.y-20) Collision = true;
    //                     if(f.x == t.x && f.y == t.y) Collision = true;

    //                     if(Collision){
    //                         switch(t.TileType){
    //                             case 'B': 
    //                                 f.IsAlive = false;
    //                                 t.IsActive= false;
    //                                 break;
    //                             case 'S': 
    //                                 f.IsAlive = false;
    //                                 // t.IsActive= false;
    //                                 break;
    //                         }
    //                         break outerloop;
    //                     }
                        
                            
    //                 }
    //             }
    //         }
    //     }
        
    //     Collision = false;
    //     outerloop:
    //     for(Gremlin g: l.Gremlins){
    //         for(Fireball f:l.Fireballs){
    //             if(f.Direction == LEFT && f.x == g.x+20 && f.y == g.y) Collision = true;
    //             if(f.Direction == UP && f.x == g.x && f.y == g.y+20) Collision = true;
    //             if(f.Direction == RIGHT && f.x == g.x-20 && f.y == g.y) Collision = true;
    //             if(f.Direction == DOWN && f.x == g.x && f.y == g.y-20) Collision = true;
    //             if(f.x == g.x && f.y == g.y) Collision = true;
    //             if(Collision){
    //                 g.IsAlive = false;
    //                 f.IsAlive= false;
    //                 break outerloop;
    //             }
    //         }
            
    //     }
    //     // return Collision;
        
    // }
    // boolean CheckEntityCollision(Entity e){
    //     Level l = Levels.get(currentLevel);
    //     boolean Collision = false;
    //     if(e.isGridLocked()){
    //         outerloop:
    //         for(int row = 0;row<NUM_ROWS;row++){
    //             for(int col = 0;col<NUM_COLS;col++){
    //                 Tile t = l.Tiles.get(row).get(col);
    //                 if(t.IsActive){
        
    //                     if(e.Direction == LEFT && e.x == t.x+20 && e.y == t.y) Collision = true;
    //                     if(e.Direction  == UP && e.x == t.x && e.y == t.y+20) Collision = true;
    //                     if(e.Direction  == RIGHT && e.x == t.x-20 && e.y == t.y) Collision = true;
    //                     if(e.Direction  == DOWN && e.x == t.x && e.y == t.y-20) Collision = true;
                    
    //                 }
    //                 if(Collision){
    //                     // l.Wizard.isMoving = false;
    //                     break outerloop;
    //                 }
    //             }
    //         }
    //     }
        
        
    //     return Collision;

    // }
    
}

