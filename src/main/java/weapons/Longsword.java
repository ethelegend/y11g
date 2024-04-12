package weapons;
public class Longsword extends Weapon {
    public Longsword(int hitBonus, int damageBonus) {
        this.name = "longsword";
        this.range = 5;
        this.hit = hitBonus;
        this.base = damageBonus;
        this.dice = new int[]{10};
    }
}
