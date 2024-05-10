package main;

import main.Window;
import oop.entity.Entity;
import oop.weapon.Weapon;

import javax.swing.*;

public class Attack {
    Entity player;
    Entity[] monster;
    int roomRadius;
    int entity;
    int step;
    JButton[] options;
    public Attack(Entity p, Entity[] m, int r) {
        player = p;
        monster = m;
        roomRadius = r;
        entity = m.length;
        battle();
    }

    public void battle() {
        step = 0;
        if (entity == monster.length) {
            entity = 0;
            battle();
            //manualAttack(monster[0]);
        } else {
            if (entity == 0) {
                options = new JButton[1];
                options[0] = new JButton("OK");
            }
            options[0].addActionListener(l -> {
                step++;
                automaticAttack();
            });

            automaticAttack();
        }
    }
    public void manualAttack() {
        return;
    }
    public void automaticAttack() {
        Entity enemy = monster[entity];
        switch (step) {
            case 0:
                /*  Explanation:
                    The enemies are programmed to advance towards their target, and retreat when under half health.
                    The other parts of the code is about if they are close to a wall or their target, in which case they should be stopped.
                */
                int right = (enemy.pos > player.pos) ? 1 : -1;
                if (enemy.maxHP / enemy.hp < 2) {
                    int movementTarget = player.pos + 5 * right;
                    if (Math.abs(enemy.pos - movementTarget) > enemy.speed) {
                        enemy.pos -= enemy.speed * right;
                        Window.infoPopup("The enemy moved towards you (" + Math.abs(player.pos - enemy.pos) + "ft)", options);
                    } else {
                        enemy.pos = player.pos + 5 * right;
                        Window.infoPopup("The enemy moved next to you (5ft)", options);
                    }
                } else {
                    int movementTarget = roomRadius * right;
                    if (Math.abs(enemy.pos - movementTarget) > enemy.speed) {
                        enemy.pos += enemy.speed * right;
                        Window.infoPopup("The enemy moved away from you (" + Math.abs(player.pos - enemy.pos) + "ft)", options);
                    } else {
                        enemy.pos = player.pos + 5 * right;
                        Window.infoPopup("The enemy moved against the wall (" + Math.abs(player.pos - enemy.pos) + "ft)", options);
                    }
                }
                break;
            case 1:
                Window.infoPopup("The enemy was too far away to attack", options); // This gets wiped if a weapon is found
                for (Weapon w : enemy.weapons) {
                    if (w.canAttack(enemy.pos - player.pos)) {
                        int damage = w.attack(player.ac);
                        Window.infoPopup("The enemy attacked with their " + w.name + " and " + ((damage == 0) ? "missed" : "dealt " + damage + " damage"), options);
                        player.hp -= damage;
                        break;
                    }
                }
                break;
            case 2:
                if (player.hp > 0) {
                    options[0].addActionListener(l -> {
                        entity++;
                        battle();
                    });
                    Window.infoPopup("You are now at " + player.hp + " health", options);
                } else {
                    options[0].addActionListener(l -> {
                        System.exit(0);
                    });
                    Window.infoPopup("You have passed out", options);

                }
        }
    }
}
