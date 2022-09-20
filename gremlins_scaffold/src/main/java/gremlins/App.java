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
    public static final int WIDTH = 720;
    public static final int HEIGHT = 720;
    public static final int SPRITESIZE = 20;
    public static final int BOTTOMBAR = 60;
    public static final int FPS = 60;
    public static final Random randomGenerator = new Random();
    public String configPath;

    ArrayList<ArrayList<Character>> tileCharArray = new ArrayList< ArrayList<Character> >();
    ArrayList<ArrayList<Tile>> Tiles = new ArrayList< ArrayList<Tile> >();
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
        String WizardImgStrs[] = {"wizard0.png","wizard1.png","wizard2.png","wizard3.png"};
        for(String s : WizardImgStrs){
            WizardImgs.add(loadImage(this.getClass().getResource(s).getPath().replace("%20", "")));
        }

        PImage StonewallImg = loadImage(this.getClass().getResource("stonewall.png").getPath().replace("%20", ""));
        PImage GremlinImg = loadImage(this.getClass().getResource("gremlin.png").getPath().replace("%20", ""));
        PImage ExitImg = loadImage(this.getClass().getResource("slime.png").getPath().replace("%20", ""));
        PImage FireballImg = loadImage(this.getClass().getResource("fireball.png").getPath().replace("%20", ""));
        
        File f = new File("level1.txt");
        try{
            Scanner scan = new Scanner(f);
            for(int row = 0; row<33;row++){
                String line = scan.nextLine();
                tileCharArray.add(new ArrayList<Character>());
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
        // Object j = conf.getJSONArray("levels").get(0);
        // System.out.println(j.getClass());
        // JSONObject j = conf.getJSONArray("levels").get(0);
        // System.out.println(j.);
        


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
        this.Wizard.tick();
        this.image(Wizard.Image, Wizard.x, Wizard.y);
        
        for(int row = 0; row<33;row++){
            for(int col = 0; col<36;col++){
                Tile t = Tiles.get(row).get(col);
                if(t.Image != null){
                    this.image(t.Image, t.x, t.y);
                }         
            }
        }
    }

    // Key event triggered when any key is pressed
    @Override
    public void keyPressed(KeyEvent e) {
        if(Wizard.target[0] == Wizard.x && Wizard.target[1] == Wizard.y){
            Wizard.TargetTile(e.getKeyCode());
        }
        // int key = e.getKeyCode();
        // Wizard.move(key);
    }

    //  Key event triggered when any key is released
    // @Override
    // public void keyReleased(KeyEvent e) {
    //     int key = e.getKeyCode();
    //     if (key == 37 || key == 39) {
    //         p2.stop();
    //     } else if (key == 65 || key == 68) {
    //         p1.stop();
    //     }
    // }

    public static void main(String[] args) {
        PApplet.main("gremlins.App");
    }
}
