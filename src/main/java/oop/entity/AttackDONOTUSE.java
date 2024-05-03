package oop.entity;

import oop.weapon.Weapon;

import javax.swing.*;
import java.awt.*;

public class AttackDONOTUSE {
    JFrame window;
    Entity attacker;
    Entity defender;
    int roomRadius;
    JButton confirm;
    int i;
    public void Attack(JFrame w, Entity a, Entity d, int r) {
        this.window = w;
        this.attacker = a;
        this.defender = d;
        this.roomRadius = r;
    }
    public void automaticAttack() {
        switch (i) {
            case 0:
                confirm = new JButton("OK");
                confirm.addActionListener(l -> {
                    this.automaticAttack();
                });
            /*  Explanation:
                The enemies are programmed to advance towards their target, and retreat when under half health.
                The other parts of the code is about if they are close to a wall or their target, in which case they should be stopped.
            */
                int right = (attacker.pos > defender.pos) ? 1 : -1;
                if (attacker.maxHP / attacker.hp < 2) {
                    int movementTarget = defender.pos + 5 * right;
                    if (Math.abs(attacker.pos - movementTarget) > attacker.speed) {
                        attacker.pos -= attacker.speed * right;
                        InfoPopup("The enemy moved towards you (" + Math.abs(defender.pos - attacker.pos) + "ft)", new JButton[]{confirm});
                    } else {
                        attacker.pos = defender.pos + 5 * right;
                        InfoPopup("The enemy moved next to you (5ft)", new JButton[]{confirm});
                    }
                } else {
                    int movementTarget = roomRadius * right;
                    if (Math.abs(attacker.pos - movementTarget) > attacker.speed) {
                        attacker.pos += attacker.speed * right;
                        InfoPopup("The enemy moved away from you (" + Math.abs(defender.pos - attacker.pos) + "ft)", new JButton[]{confirm});
                    } else {
                        attacker.pos = defender.pos + 5 * right;
                        InfoPopup("The enemy moved against the wall (" + Math.abs(defender.pos - attacker.pos) + "ft)", new JButton[]{confirm});
                    }
                }
            case 1:
                InfoPopup("The enemy was too far away to attack", new JButton[] {confirm});
                for (Weapon w : attacker.weapons) {
                    if (w.canAttack(attacker.pos = defender.pos)) {
                        int damage = w.attack(defender.ac);
                        InfoPopup("The enemy attacked with their " + w.name + " and dealt " + damage + " damage", new JButton[] {confirm});
                        defender.hp -= damage;
                    }
                }
                break;
            case 2:
                if (defender.hp > 0) {

                }
        }
        i++;
    }
    public void InfoPopup(String labelText, JButton[] button) {
        this.window.getContentPane().removeAll();
        this.window.setLayout(new GridLayout(2,1));
        this.window.add(new JLabel(labelText, SwingConstants.CENTER));

        Container buttons = new Container();
        this.window.add(buttons);
        buttons.setLayout(new GridLayout());
        for (JButton b : button) {
            buttons.add(b);
        }

        this.window.setSize(button.length * 100, 200);
        this.window.repaint();
        this.window.revalidate();
    }
}
