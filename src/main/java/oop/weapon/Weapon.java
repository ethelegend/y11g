package oop.weapon;
import util.Dice;

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
        return (ac > Dice.rollCheck(this.hit))
                ? 0
                : Dice.rollDice(this.base,this.dice);
    }
}
