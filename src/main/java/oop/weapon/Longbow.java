package oop.weapon;

public class Longbow extends Weapon {
    public Longbow(int hitBonus, int damageBonus) {
        this.name = "Longbow";
        this.range = 150;
        this.hit = hitBonus;
        this.base = damageBonus;
        this.dice = new int[]{8};
    }
}
