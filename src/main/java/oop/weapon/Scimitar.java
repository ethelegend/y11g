package oop.weapon;

public class Scimitar extends Weapon {
    public Scimitar(int hitBonus, int damageBonus) {
        this.name = "Scimitar";
        this.range = 5;
        this.hit = hitBonus;
        this.base = damageBonus;
        this.dice = new int[]{6};
    }
}
