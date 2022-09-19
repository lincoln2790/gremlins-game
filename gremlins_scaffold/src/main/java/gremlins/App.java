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

public class App extends PApplet {
    public static final int WIDTH = 720;
    public static final int HEIGHT = 720;
    public static final int SPRITESIZE = 20;
    public static final int BOTTOMBAR = 60;
    public static final int FPS = 60;
    public static final Random randomGenerator = new Random();
    public String configPath;

    // public PImage brickwall;
    // public PImage stonewall;
    // public PImage exit;

    ArrayList<ArrayList<Character>> tileCharArray = new ArrayList< ArrayList<Character> >();
    ArrayList<ArrayList<Tile>> tiles = new ArrayList< ArrayList<Tile> >();
    Entity wizard;
    





    public App() {
        this.configPath = "config.json";
    }

    /**
     * Initialise the setting of the window size.
    */
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player, enemies and map elements.
    */
    public void setup() {
        frameRate(FPS);
// "brickwall_destroyed0.png"
// "brickwall_destroyed1.png"
// "brickwall_destroyed2.png"
// "brickwall_destroyed3.png"
        // Load images during setup
        ArrayList< PImage> brickwallSequence = new ArrayList<PImage>();
        ArrayList< PImage> wizardSequence = new ArrayList<PImage>();
        String brickwallSeqStrs[] = {"brickwall.png", "brickwall_destroyed0.png","brickwall_destroyed1.png","brickwall_destroyed2.png","brickwall_destroyed3.png"};
        String wizardSeqStrs[] = {"wizard0.png","wizard1.png","wizard2.png","wizard3.png"};
        for(String s : brickwallSeqStrs){
            brickwallSequence.add(loadImage(this.getClass().getResource(s).getPath().replace("%20", "")));
        }
        for(String s : wizardSeqStrs){
            wizardSequence.add(loadImage(this.getClass().getResource(s).getPath().replace("%20", "")));
        }


        PImage stonewall = loadImage(this.getClass().getResource("stonewall.png").getPath().replace("%20", ""));
        PImage gremlin = loadImage(this.getClass().getResource("gremlin.png").getPath().replace("%20", ""));
        PImage exit = loadImage(this.getClass().getResource("slime.png").getPath().replace("%20", ""));
        PImage fireball = loadImage(this.getClass().getResource("fireball.png").getPath().replace("%20", ""));
        
        File f = new File("level1.txt");
        try{
            Scanner scan = new Scanner(f);
            Entity w;
            for(int row = 0; row<33;row++){
                String line = scan.nextLine();
                tileCharArray.add(new ArrayList<Character>());
                tiles.add(new ArrayList<Tile>());
                for(int col = 0; col<36;col++){
                    Character c = line.charAt(col);
                    tileCharArray.get(row).add(c);
                    Tile t;
                    switch(c){
                        case 'X':
                            t = new StoneWall(col*20,row*20,stonewall);
                            break;
                        case 'B':
                            t = new BrickWall(col*20,row*20, brickwallSequence);
                            break;
                        case 'E':
                            t = new Exit(col*20,row*20,exit);
                            break;
                            
                        case 'W':
                            wizard = new Wizard(col*20,row*20, wizardSequence);
                            
                        default:
                            t = new Tile(0,0);
                        

                    }
                    tiles.get(row).add(t);
                }
                System.out.println("");
            }
            for(int row = 0; row<33;row++){
                for(int col = 0; col<36;col++){
                    System.out.print(tileCharArray.get(row).get(col));
                    // tiles.
                }
                System.out.println("");
            }

        //     for(int i = 0; i<20;i++){
        //         for(int j = 0; j<20;j++){
        //             System.out.println("j");
        //         }System.out.println();
        //     }
        //     // while(scan.hasNext()){
        //     //     tiles
        //     //     
            // }    
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        
        JSONObject conf = loadJSONObject(new File(this.configPath));
        // Object j = conf.getJSONArray("levels").get(0);
        // System.out.println(j.getClass());
        // JSONObject j = conf.getJSONArray("levels").get(0);
        // System.out.println(j.);
        


    }

    /**
     * Receive key pressed signal from the keyboard.
    */
    public void keyPressed(){

    }
    
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
        this.image(wizard.Image, wizard.x, wizard.y);
        for(int row = 0; row<33;row++){
            for(int col = 0; col<36;col++){
                // System.out.print(tileCharArray.get(row).get(col));
                // tiles.
                Tile t = tiles.get(row).get(col);
                if(t.Image != null){
                    this.image(t.Image, t.x, t.y);
                }
                
            }
            // System.out.println("");
        }
        // this.image(tiles[4][4].Image,tiles[4][4].x, tiles[4][4].y);
        
        // for(int i = 0; i<33;i++){
        //     for(int j = 0; j<36;j++){
        //         this.image(tiles[i][j].Image,tiles[i][j].x, tiles[i][j].y);
        //         // System.out.println("j");
        //     }
        //     // System.out.println();
        // }
    }

    public static void main(String[] args) {
        PApplet.main("gremlins.App");
    }
}
