package weapons;

public class Bite extends Weapon {
    public Bite(int hitBonus, int damageBonus, int[] dice) {
        this.range = 5;
        this.hit = hitBonus;
        this.base = damageBonus;
        this.dice = dice;
    }
}
