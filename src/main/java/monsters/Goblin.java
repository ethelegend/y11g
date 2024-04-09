package monsters;
import main.Dice;
import weapons.Weapon;
import weapons.Scimitar;
import weapons.Shortbow;


public class Goblin extends Entity {
    public Goblin() {
        this.ac = 15;
        this.speed = 30;
        this.hp = Dice.RollDice(0,new int[]{6,6});
        this.weapons = new Weapon[]{
                new Scimitar(4,2),
                new Shortbow(4,2)
        };
    }
}
