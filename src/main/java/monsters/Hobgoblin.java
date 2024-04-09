package monsters;
import main.Dice;
import weapons.Weapon;
import weapons.Longsword;
import weapons.Longbow;


public class Hobgoblin extends Entity {
    public Hobgoblin() {
        this.ac = 18;
        this.speed = 30;
        this.hp = Dice.RollDice(2,new int[]{8,8});
        this.weapons = new Weapon[]{
                new Longsword(4,1),
                new Longbow(4,1)
        };
    }
}
