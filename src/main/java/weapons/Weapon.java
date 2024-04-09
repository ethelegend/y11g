package weapons;
import main.Dice;

public class Weapon {
    public int range;
    int hit;
    int base;
    int[] dice;
    public boolean CanAttack(int distance) {
        return (distance <= this.range);
    }
    public int Attack(int ac) {
        return (ac > Dice.RollDice(this.hit, new int[]{20}))
                ? 0
                : Dice.RollDice(this.base,this.dice);
    }
}
