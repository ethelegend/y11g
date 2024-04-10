package monsters;

import org.json.simple.JSONObject;
import weapons.Weapon;

import javax.swing.*;

public class Entity {
    int ac;
    int speed;
    int hp;
    int maxHP;
    Weapon[] weapons;
    int pos;
    boolean player;
    public String Attack(JFrame window, Entity target, int roomRadius) {
        if (player) {
            return this.ManualAttack(window, target, roomRadius);
        } else {
            return this.AutomaticAttack(window, target, roomRadius);
        }

    }
    public String ManualAttack(JFrame window, Entity target, int roomRadius) {
        return null;
    }
    public String AutomaticAttack(JFrame window, Entity target, int roomRadius) {
        /*  Explanation:
            The enemies are programmed to advance towards their target, and retreat when under half health.
            The other parts of the code is about if they are close to a wall or their target, in which case they should be stopped.
        */
        int right = (this.pos > target.pos) ? 1 : -1;
        int movementTarget = (this.maxHP/this.hp < 2)
                ? target.pos + 5 * right
                : roomRadius * right;
        right = (movementTarget > this.pos) ? 1 : -1;
        this.pos = (maxHP/hp < 2) // If you are over half health
                ? (Math.abs(this.pos - target.pos) > this.speed + 5) // If you are not within movement range of the enemy
                        ? this.pos - this.speed * right // Move towards the target
                        : target.pos + 5 * right // Move next to the target
                : (roomRadius - this.pos * right > this.speed) // If you are not within movement range of the wall
                        ? this.pos + this.speed * right // Move away from the target
                        : roomRadius * right; // Move against the wall
        int damage;
        for (Weapon weapon : this.weapons) {
            if (weapon.range >= Math.abs(this.pos - target.pos)) {
                damage = weapon.Attack(target.ac);
            }
        }

        return null;
    }

    public int InfoPopup(JFrame window, String labelText, String[] buttonText) {
        return 0;
    }
}
