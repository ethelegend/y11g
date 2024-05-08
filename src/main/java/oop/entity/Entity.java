package oop.entity;

import oop.weapon.Weapon;
import main.Window;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Container;
import java.awt.GridLayout;

public class Entity {
    int ac;
    int speed;
    int hp;
    int maxHP;
    Weapon[] weapons;
    int pos;
    boolean player;
    int i;
    Entity target;
    int roomRadius;
    public void attack(Entity t, int r, boolean p) {
        i = 0;
        target = t;
        roomRadius = r;
    }
    public void manualAttack(Entity t, int r) {
        return;
    }
    public void automaticAttack(Entity t, int r) {
        JButton confirm;
        if (t != null) {
            i = 0;
            target = t;
            roomRadius = r;
            confirm = new JButton("OK");
            confirm.addActionListener(l -> {
                i++;
                automaticAttack(null, 0);
            });
        }
        switch (i) {
            case 0:
                /*  Explanation:
                    The enemies are programmed to advance towards their target, and retreat when under half health.
                    The other parts of the code is about if they are close to a wall or their target, in which case they should be stopped.
                */
                int right = (this.pos > target.pos) ? 1 : -1;
                if (this.maxHP / this.hp < 2) {
                    int movementTarget = target.pos + 5 * right;
                    if (Math.abs(this.pos - movementTarget) > this.speed) {
                        this.pos -= this.speed * right;
                        Window.infoPopup("The enemy moved towards you (" + Math.abs(target.pos - this.pos) + "ft)", new JButton[]{confirm});
                    } else {
                        this.pos = target.pos + 5 * right;
                        Window.infoPopup("The enemy moved next to you (5ft)", new JButton[]{confirm});
                    }
                } else {
                    int movementTarget = roomRadius * right;
                    if (Math.abs(this.pos - movementTarget) > this.speed) {
                        this.pos += this.speed * right;
                        Window.infoPopup("The enemy moved away from you (" + Math.abs(target.pos - this.pos) + "ft)", new JButton[]{confirm});
                    } else {
                        this.pos = target.pos + 5 * right;
                        Window.infoPopup("The enemy moved against the wall (" + Math.abs(target.pos - this.pos) + "ft)", new JButton[]{confirm});
                    }
                }
                break;
            case 1:
                Window.infoPopup("The enemy was too far away to attack", new JButton[] {confirm}); // This gets wiped if a weapon is found
                for (Weapon w : this.weapons) {
                    System.out.println(w);
                    if (w.canAttack(this.pos - target.pos)) {
                        int damage = w.attack(target.ac);
                        Window.infoPopup("The enemy attacked with their " + w.name + " and dealt " + damage + " damage", new JButton[] {confirm});
                        target.hp -= damage;
                        break;
                    }
                }
                break;
            case 2:
                if (target.hp > 0) {
                } else {
                    confirm.addActionListener(l -> {
                        System.exit(0);
                    });
                    Window.infoPopup("You have passed out", new JButton[] {confirm});

                }
        }
    }
}