package oop.weapon;
import main.Dice;

public class Weapon {
    public String name;
    public int range;
    int hit;
    int base;
    int[] dice;
    public boolean canAttack(int distance) {
        System.out.println(Math.abs(distance) <= this.range);
        return (Math.abs(distance) <= this.range);
    }
    public int attack(int ac) {
        return (ac > Dice.rollDice(this.hit, new int[]{20}))
                ? 0
                : Dice.rollDice(this.base,this.dice);
    }
}
