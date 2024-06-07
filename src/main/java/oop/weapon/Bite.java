package oop.weapon;

public class Bite extends Weapon {
    public Bite(int hitBonus, int damageBonus, int[] dice) {
        this.name = "Bite";
        this.range = 5;
        this.hit = hitBonus;
        this.base = damageBonus;
        this.dice = dice; // Unique because different creatures' bites deal different damage
    }
}