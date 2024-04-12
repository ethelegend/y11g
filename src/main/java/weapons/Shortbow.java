package weapons;

public class Shortbow extends Weapon {
    public Shortbow(int hitBonus, int damageBonus) {
        this.name = "shortbow";
        this.range = 80;
        this.hit = hitBonus;
        this.base = damageBonus;
        this.dice = new int[]{6};
    }
}
