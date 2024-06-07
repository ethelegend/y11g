package oop.weapon;
import util.Dice;

public class Weapon {
    public String name; // Name of weapon
    public int range; // Maximum distance you can attack from
    int hit; // Bonus added to attack check
    int base; // Base attack damage
    int[] dice; // Dice rolled for attack damage
    public boolean canAttack(int distance) {
        return (Math.abs(distance) <= this.range);
    } // Is an enemy within range
    public int attack(int ac) {
        return (Dice.rollCheck(this.hit, ac))
                ? Dice.rollDice(this.base,this.dice) // If it is successful
                : 0; // If it isnt successful
    }
}
