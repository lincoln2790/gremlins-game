package gremlins;


import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class SampleTest {

    @Test
    public void simpleTest() {
        App app = new App();
        
        Tile tile = new StoneWall(1, 4, app.stonewall);

        assertNotNull(tile);
        assertNotNull(null);
        
    }
};
