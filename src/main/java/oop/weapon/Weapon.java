/*
    Same idea as the Entity class, but for weapons.
*/

package oop.weapon;
import main.Static;

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
        return (Static.rollCheck(this.hit, ac))
                ? Static.rollDice(this.base,this.dice) // If it is successful, return damage
                : 0; // If it isn't successful, return 0
    }
}