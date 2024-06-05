package oop.weapon;
public class Greatsword extends Weapon {
    public Greatsword(int hitBonus, int damageBonus) {
        this.name = "Greatsword";
        this.range = 5;
        this.hit = hitBonus;
        this.base = damageBonus;
        this.dice = new int[]{6,6};
    }
}
