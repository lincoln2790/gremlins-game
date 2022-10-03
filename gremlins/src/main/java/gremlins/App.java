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

    public static final Random randomGenerator = new Random();
    public String configPath;

    ArrayList<Level> Levels = new ArrayList<Level>();
    int currentLevel = 1;

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
        String WizardImgStrs[] = {"wizard0.png","wizard1.png","wizard2.png","wizard3.png","fireball.png"};
        for(String s : WizardImgStrs){
            WizardImgs.add(loadImage(this.getClass().getResource(s).getPath().replace("%20", "")));
        }

        // Loading all other images
        PImage StonewallImg = loadImage(this.getClass().getResource("stonewall.png").getPath().replace("%20", ""));
        PImage GremlinImg = loadImage(this.getClass().getResource("gremlin.png").getPath().replace("%20", ""));
        PImage ExitImg = loadImage(this.getClass().getResource("slime.png").getPath().replace("%20", ""));
        PImage FireballImg = loadImage(this.getClass().getResource("fireball.png").getPath().replace("%20", ""));
        
        // for(int i = 0; i<5;i++){
        //     Fireballs.add(new Fireball(0, 0, ' ', FireballImg));
        // }
        JSONObject conf = loadJSONObject(new File(this.configPath));
 
        for(int i = 0; i< conf.getJSONArray("levels").size();i++){
            JSONObject level = conf.getJSONArray("levels").getJSONObject(i);
            String levelStr = level.getString("layout");
            Float levelWizardCooldown = level.getFloat("wizard_cooldown");
            Float  levelEnemyCooldown = level.getFloat("enemy_cooldown");
            Level levelObj = new Level(levelStr,levelWizardCooldown, levelEnemyCooldown);
            File f = new File(levelStr);
            Levels.add(levelObj);
            try{
                Scanner scan = new Scanner(f);
                for(int row = 0; row<33;row++){
                    String line = scan.nextLine();
                    
                    Levels.get(i).Tiles.add(new ArrayList<Tile>());
                    for(int col = 0; col<NUM_COLS;col++){
                        Character c = line.charAt(col);
                        Tile t =new Tile(col*20,row*20);
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
                                Wizard w = new Wizard(col*20,row*20, WizardImgs);
                                Levels.get(i).Wizard = w;
                                break;
                            case 'G':
                                Gremlin g = new Gremlin(col*20,row*20, GremlinImg);
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
        Level l = Levels.get(currentLevel-1);
        
        this.image(l.Wizard.Sequence.get(l.Wizard.ImageIndex), l.Wizard.x, l.Wizard.y);

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
        for(int i = 0; i< l.Gremlins.size(); i++){
            // if( l.Gremlins.get(i).Image != null){
            this.image(l.Gremlins.get(i).Image, l.Gremlins.get(i).x, l.Gremlins.get(i).y);
            // }
        }
    }    // Key event triggered when any key is pressed
    @Override
    public void keyPressed(KeyEvent e) {
     
    }
    public void keyReleased(){
      
    }
   
    public static void main(String[] args) {
        PApplet.main("gremlins.App");
    }
}

