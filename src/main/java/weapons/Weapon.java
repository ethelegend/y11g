package weapons;
import main.Dice;

public class Weapon {
    int range;
    int hit;
    int base;
    int[] dice;
    public boolean CanAttack(int distance) {
        return (distance <= this.range);
    }
    public int Attack(int ac) {
        int roll = (int) Math.floor(Math.random()*20 + 1) + this.hit;
        if (ac > Dice.RollDice(this.hit,new int[]{20})) {return 0;}
        return Dice.RollDice(this.base,this.dice);
    }
}
