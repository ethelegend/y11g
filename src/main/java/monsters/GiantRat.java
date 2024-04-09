package monsters;
import main.Dice;
import weapons.Weapon;
import weapons.Bite;


public class GiantRat extends Entity {
    public GiantRat() {
        this.ac = 12;
        this.speed = 30;
        this.hp = Dice.RollDice(0,new int[]{6,6});
        this.weapons = new Weapon[]{
                new Bite(4,2,new int[]{4})
        };
    }
}

