package weapons;
import main.Dice;

public class Weapon {
    public String name;
    public int range;
    int hit;
    int base;
    int[] dice;
    public boolean canAttack(int distance) {
        return (Math.abs(distance) <= this.range);
    }
    public int attack(int ac) {
        return (ac > Dice.RollDice(this.hit, new int[]{20}))
                ? 0
                : Dice.RollDice(this.base,this.dice);
    }
}
