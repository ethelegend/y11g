package monsters;
import main.Dice;
import weapons.Weapon;
import weapons.Bite;


public class Wolf extends Entity {
    public Wolf() {
        this.ac = 13;
        this.speed = 40;
        this.hp = Dice.RollDice(2,new int[]{8,8});
        this.weapons = new Weapon[]{
                new Bite(4,2,new int[]{4,4})
        };
    }
}

