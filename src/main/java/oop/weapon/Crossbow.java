package oop.weapon;

public class Crossbow extends Weapon {
    public Crossbow(int hitBonus, int damageBonus) {
        this.name = "Crossbow";
        this.range = 400;
        this.hit = hitBonus;
        this.base = damageBonus;
        this.dice = new int[]{10};
    }
}
