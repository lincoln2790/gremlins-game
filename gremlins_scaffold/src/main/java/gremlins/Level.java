package gremlins;
import processing.core.PImage;
class Level{
    public
        String Layout;
        float WizardCooldown;
        float EnemyCooldown;
        Level(String Layout, float WizardCooldown, float EnemyCooldown){
            this.Layout= Layout;
            this.WizardCooldown= WizardCooldown;
            this.EnemyCooldown = EnemyCooldown;
        }
};