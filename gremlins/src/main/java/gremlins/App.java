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
    // private static final 
    private static final int WIDTH = 720;
    private static final int HEIGHT = 720;
    private static final int SPRITESIZE = 20;
    private static final int FPS = 60;
    private static final int NUM_ROWS = 33;
    private static final int NUM_COLS = 36;
    private String configPath;
    private ArrayList<Level> Levels = new ArrayList<Level>();
    private int keyCode;
    private int keysPressed[] = {0,0,0,0};
    boolean spaceHeld;
    private boolean GameOver  = false;
    private boolean GameWin  = false;
    private int currentLevel = 0;
    
    Random rand = new Random();
    private static final String WizardImgStrs[] = {"wizard0.png","wizard2.png","wizard1.png","wizard3.png","fireball.png"};
    private static final String BrickwallImgStrs[] = {"brickwall.png", "brickwall_destroyed0.png","brickwall_destroyed1.png","brickwall_destroyed2.png","brickwall_destroyed3.png"};
    public App() {
        this.configPath = "config.json";
    }

    public void settings() {
        size(WIDTH, HEIGHT);
    }

    public void setup() {
        frameRate(FPS);
        ArrayList< PImage> BrickwallImgs = new ArrayList<PImage>();
        for(String s : BrickwallImgStrs)
            BrickwallImgs.add(loadImage(this.getClass().getResource(s).getPath().replace("%20", "")));
        
        // Loading Wizard animation images
        ArrayList< PImage> WizardImgs = new ArrayList<PImage>(); 
        for(String s : WizardImgStrs)
            WizardImgs.add(loadImage(this.getClass().getResource(s).getPath().replace("%20", "")));
        

        // Loading all other images
        PImage StonewallImg = loadImage(this.getClass().getResource("stonewall.png").getPath().replace("%20", ""));
        PImage GremlinImg = loadImage(this.getClass().getResource("gremlin.png").getPath().replace("%20", ""));
        PImage ExitImg = loadImage(this.getClass().getResource("exit.png").getPath().replace("%20", ""));
        PImage SlimeImg = loadImage(this.getClass().getResource("slime.png").getPath().replace("%20", ""));
        PImage FireballImg = loadImage(this.getClass().getResource("fireball.png").getPath().replace("%20", ""));
        PImage blueFireballImg = loadImage(this.getClass().getResource("blueFire.png").getPath().replace("%20", ""));
        
        PImage BurgerImg = loadImage(this.getClass().getResource("burger.png").getPath().replace("%20", ""));

        JSONObject conf = loadJSONObject(new File(this.configPath));
        for(int i = 0; i< conf.getJSONArray("levels").size();i++){
            JSONObject level = conf.getJSONArray("levels").getJSONObject(i);
            String levelStr = level.getString("layout");
            Float wizardCooldown = level.getFloat("wizard_cooldown");
            Float  enemyCooldown = level.getFloat("enemy_cooldown");
            Level levelObj = new Level(levelStr);
            for(int f = 0; f< Level.MAX_FIREBALLS;f++){
                levelObj.Fireballs.add(new Fireball(0, 0, FireballImg,blueFireballImg));
            }
            levelObj.Burger = new Power(-SPRITESIZE,-SPRITESIZE,BurgerImg);
            Levels.add(levelObj);

            File f = new File(levelStr);
            try{
                Scanner scan = new Scanner(f);
                for(int row = 0; row<NUM_ROWS;row++){
                    String line = scan.nextLine();
                    Levels.get(i).Tiles.add(new ArrayList<Tile>());
                    for(int col = 0; col<NUM_COLS;col++){
                        Character c = line.charAt(col);
                        Tile t =new Tile(col*SPRITESIZE,row*SPRITESIZE);
                        switch(c){
                            case 'X':
                                t = new StoneWall(col*SPRITESIZE,row*SPRITESIZE,StonewallImg);
                                break;
                            case 'B':
                                t = new BrickWall(col*SPRITESIZE,row*SPRITESIZE, BrickwallImgs);
                                break;
                            case 'E':
                                Entity e = new Exit(col*SPRITESIZE,row*SPRITESIZE,ExitImg);
                                Levels.get(i).Exit = e;
                                break;
                            case 'W':
                                Wizard w = new Wizard(col*SPRITESIZE,row*SPRITESIZE, WizardImgs, wizardCooldown);
                                w.Lives = conf.getInt("lives");
                                Levels.get(i).Wizard = w;
                                break;
                            case 'G':
                                Gremlin g = new Gremlin(col*SPRITESIZE,row*SPRITESIZE, GremlinImg, enemyCooldown);
                                for(int it = 0; it< Level.MAX_FIREBALLS;it++){
                                    g.Slimeballs.add(new Slimeball(0, 0, SlimeImg));
                                }
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
        // If game ends, end screen drawn
        this.background(192,156,124);
        if(GameOver){
            textSize(40);
            fill(255,255,255);
            text("GAME OVER!",NUM_COLS*7, NUM_COLS*10);
            return;
        }
        if(GameWin){
            textSize(40);
            fill(255,255,255);
            text("YOU WIN!",NUM_COLS*7, NUM_COLS*10);
            return;
        }

        // Background and text drawn
        this.background(192,156,124);
        textSize(17);
        fill(255,255,255);
        text("Lives:",10, (NUM_ROWS+1)*SPRITESIZE+10);
        text(String.format("Level %d/2",currentLevel+1),SPRITESIZE*8+10, (NUM_ROWS+1)*SPRITESIZE+10);

        Level l = Levels.get(currentLevel);
        // Wizard Cooldown box drawn
        if(l.Wizard.cooldownTimer>0){
            fill(255,255,255);
            rect(SPRITESIZE*30+10, (NUM_ROWS+1)*SPRITESIZE+10,99,10);
            fill(0,0,0);
            rect(SPRITESIZE*30+10, (NUM_ROWS+1)*SPRITESIZE+10,99-l.Wizard.cooldownTimer*3,10);
        }
        
        // Number of lives drawn
        for(int i = 0;i < l.Wizard.Lives;i++){
            this.image(l.Wizard.Sequence.get(2),(3+i)*SPRITESIZE, (NUM_COLS-2)*SPRITESIZE);
        }

        // Wizard drawn
        for(int i = 0; i<4;i++){
            l.Wizard.tick(CheckEntityWallCollision(l.Wizard));
        }
        this.image(l.Wizard.Image, l.Wizard.x, l.Wizard.y);

        // Exit Drawn, if wizard reaches exit, next level or game win
        if(CheckEntitiesCollision(l.Wizard, l.Exit)){
            currentLevel++;
            if(currentLevel + 1 > Levels.size()){
                GameWin = true;
                return;
            }
        };
        this.image(l.Exit.Image, l.Exit.x, l.Exit.y);

        // Tiles drawn
        for(int row = 0; row<NUM_ROWS;row++){
            for(int col = 0; col<NUM_COLS;col++){
                Tile t = l.Tiles.get(row).get(col);
                t.tick();
                if(t.Image != null){
                    this.image(t.Image, t.x, t.y);
                }

            }
        }
        

        // Fireballs drawn
        for(Fireball f: l.Fireballs){

            if(f.IsAlive){
                if(l.Wizard.PowerActive){
                    this.image(f.powerImage, f.x, f.y);
                }else {
                    this.image(f.Image, f.x, f.y);
                }
                
                for(int i = 0; i<f.VELOCITY;i++){
                    boolean  ni = CheckEntityWallCollision(f);

                    f.tick(ni);
                
                }

                for(Gremlin g: l.Gremlins){
                    if(g.IsAlive && CheckEntitiesCollision(f, g)){
  
                        boolean whileCond = true;
                        while(whileCond){
                            int randX = rand.nextInt(NUM_COLS);
                            int randY = rand.nextInt(NUM_ROWS);
                            Tile t = l.Tiles.get(randY).get(randX);
                            if(!t.IsActive && abs(l.Wizard.x - randX*SPRITESIZE)> 10*SPRITESIZE && abs(l.Wizard.y - randY*SPRITESIZE)> 10*SPRITESIZE){
                                g.x = randX*SPRITESIZE;
                                g.y= randY*SPRITESIZE;
                                whileCond=false;
                            }
                            
                        }
                        f.IsAlive= false;
                    }   
                }
            }   
        }    

        // Gremlins drawn
        for(Gremlin g: l.Gremlins){
            if(g.IsAlive){
                this.image(g.Image,g.x, g.y);
                g.tick(CheckEntityWallCollision(g));
                if(CheckEntitiesCollision(g, l.Wizard)){
                    reset();
                }
                
                if(l.EnemyCooldownTimer >0){
                    for(int i = 0; i<Level.MAX_FIREBALLS;i++){
                        if(g.Slimeballs.get(i).IsAlive){
                            if(CheckEntitiesCollision(g.Slimeballs.get(i), l.Wizard)){
                                reset();
                                return;
                            }
                            for(int j =0; j<Level.MAX_FIREBALLS;j++){
                                if(CheckEntitiesCollision(g.Slimeballs.get(i), l.Fireballs.get(j))){
                                    g.Slimeballs.get(i).IsAlive = false;
                                    l.Fireballs.get(j).IsAlive = false;
                                    return;
                                }
                            }
                            
                            for(int j = 0;j<g.Slimeballs.get(i).VELOCITY;j++){
                                g.Slimeballs.get(i).tick(CheckEntityWallCollision(g.Slimeballs.get(i)));
                                this.image(g.Slimeballs.get(i).Image,g.Slimeballs.get(i).x, g.Slimeballs.get(i).y);
                            }
                        }
                        
                    }
                } else {
                    for(int i = 0; i<Level.MAX_FIREBALLS;i++){
                        if(!g.Slimeballs.get(i).IsAlive){
                            g.Slimeballs.get(i).IsAlive = true;
                            g.Slimeballs.get(i).x = g.x;
                            g.Slimeballs.get(i).y = g.y;
                            g.Slimeballs.get(i).Direction = g.Direction;
                            break;
                        }
                        
                    }
                }  
                if(l.EnemyCooldownTimer<0){
                    l.EnemyCooldownTimer = g.cooldown;
                }
            }  
        }
        
        l.EnemyCooldownTimer--;
        l.BurgerTimer--;
        if(!l.Burger.IsAlive&& l.BurgerTimer<=0){
            l.Burger.IsAlive = true;

        }
        if(l.Burger.IsAlive){
            this.image(l.Burger.Image,l.Burger.x,l.Burger.y);
            boolean whileCond = true;
            if(CheckEntitiesCollision(l.Burger, l.Wizard)){
                l.Burger.IsAlive = false;
                l.Wizard.PowerActive = true;
            }
            if(l.BurgerTimer<=0){
                while(whileCond){
                    
                    int randX = rand.nextInt(NUM_COLS);
                    int randY = rand.nextInt(NUM_ROWS);
                    Tile t = l.Tiles.get(randY).get(randX);
                    if(!t.IsActive){
                        l.Burger.x = randX*SPRITESIZE;
                        l.Burger.y= randY*SPRITESIZE;
                        whileCond=false;
                        l.BurgerTimer = 60*15;
                    }
                }
            }
        }
        if(!l.Burger.IsAlive){
            l.BurgerTimer = 60*15;
        }
        
        
    }    



    // Key event triggered when any key is pressed
    public void keyPressed(KeyEvent e) {
        if(GameOver) return;
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
        if(keyCode == 32 ||spaceHeld){
            spaceHeld = true;
            if(l.Wizard.cooldownTimer <= 0){
                for(int i = 0; i<Level.MAX_FIREBALLS;i++){
                    if(!l.Fireballs.get(i).IsAlive){
                        l.Fireballs.get(i).IsAlive = true;
                        l.Fireballs.get(i).x = l.Wizard.x;
                        l.Fireballs.get(i).y = l.Wizard.y;
                        l.Fireballs.get(i).Direction = l.Wizard.Direction;
                        l.Wizard.cooldownTimer = l.Wizard.cooldown;
                        // if(true){
                        //     l.Wizard.cooldownTimer = 0;
                        //     l.Fireballs.get(i).Direction = rand.nextInt(4)+37;
                        // }
                        break;
                    }
                }
            }
        }
    }

    // Key event triggered when any key is released
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
        if(keyCode == 32){
            spaceHeld = false;
        }
    }

// When wizard-enemy/slimeball collision occurs, level reset, life deducted
    public void reset(){
        Level l = Levels.get(currentLevel);
        l.Wizard.Lives--;
        if (l.Wizard.Lives == 0){
            GameOver = true;
        }
        l.Wizard.x = l.Wizard.startX;
        l.Wizard.y = l.Wizard.startY;
        for(int row = 0; row<NUM_ROWS;row++){
            for(int col = 0; col<NUM_COLS;col++){
                if(l.Tiles.get(row).get(col).TileType == 'B'){ 
                    l.Tiles.get(row).get(col).IsActive = true;
                    l.Tiles.get(row).get(col).AnimationIndex = 0;
                }
            }
        }
        for(int i = 0; i < l.Gremlins.size();i++){
            l.Gremlins.get(i).IsAlive = true;
            l.Gremlins.get(i).x = l.Gremlins.get(i).startX;
            l.Gremlins.get(i).y = l.Gremlins.get(i).startY;
        }
        
    }
//    Main function
    public static void main(String[] args) {
        PApplet.main("gremlins.App");
    }

// Entity Entity case of collision
    public boolean CheckEntitiesCollision(Entity  e1, Entity e2){
        boolean Collision = false;
        if(e1.x < e2.x + SPRITESIZE &&
            e1.x + SPRITESIZE > e2.x &&
            e1.y < e2.y + SPRITESIZE &&
            SPRITESIZE + e1.y > e2.y){
                Collision = true; 
        }
        return Collision;
    }

// Entity Tile case of collision
    public boolean CheckEntityWallCollision(Entity e1){
        Level l = Levels.get(currentLevel);
        boolean Collision = false;
        outerloop:
        for(int row = 0;row<NUM_ROWS;row++){
            for(int col = 0;col<NUM_COLS;col++){
                Tile t = l.Tiles.get(row).get(col);
                if(t.TileType != 'T' && t.IsActive){
                    // if(e1.x < t.x + SPRITESIZE &&
                    //     e1.x + SPRITESIZE > t.x &&
                    //     e1.y < t.y + SPRITESIZE &&
                    //     SPRITESIZE + e1.y > t.y){
                    //         Collision = true; 
                    // }
                    if(e1.Direction == LEFT && e1.x == t.x+SPRITESIZE && e1.y == t.y) Collision = true;
                    if(e1.Direction == UP && e1.x == t.x && e1.y == t.y+SPRITESIZE) Collision = true;
                    if(e1.Direction == RIGHT && e1.x == t.x-SPRITESIZE && e1.y == t.y) Collision = true;
                    if(e1.Direction == DOWN && e1.x == t.x && e1.y == t.y-SPRITESIZE) Collision = true;
                }       
                

                if(Collision && e1.type == 'F'){
                    switch(t.TileType){
                        case 'B': 
                            e1.IsAlive = false;
                            t.IsActive= false;
                            break;
                        case 'S': 
                            e1.IsAlive = false;
                            break;
                    }
                    if(l.Wizard.PowerActive){
                        for(int i = 0;i<14;i++){
                            try{
                                if(l.Tiles.get(row+l.BlueFireMatrix[i][0]).get(col+l.BlueFireMatrix[i][1]).TileType == 'B'){
                                    l.Tiles.get(row+l.BlueFireMatrix[i][0]).get(col+l.BlueFireMatrix[i][1]).IsActive = false;
                                }
                            }catch(ArrayIndexOutOfBoundsException e){
                            }catch(IndexOutOfBoundsException e){}
                    
                        }
                    }
                    
                    break outerloop;
                }
                if(Collision && e1.type == 'W' && t.TileType == 'E'){

                    break outerloop;
                }  
            }       
        }
        return Collision;
    }
}

