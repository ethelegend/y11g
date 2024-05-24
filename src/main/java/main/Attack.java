package main;

import oop.entity.Entity;
import oop.weapon.Weapon;

import javax.swing.*;

public class Attack {
    Entity player;
    Entity[] monsters;
    int roomRadius;
    int entity;
    int step;
    JButton[] options;
    public Attack(Entity p, Entity[] m, int r) {
        player = p; // The player object
        monsters = m; // Array of monster objects
        roomRadius = r; // Radius of room (e.g. r = 10: {-10, -5, 0, 5, 10}
        player.pos = -r; // The player enters the room
        entity = monsters.length; // An index for which entity is moving, monsters.length -> player, < monsters.length -> monsters[entity]
        battle(); // we love recursion
    }

    public void battle() {
        step = 0; // 2nd index for which step of the attack the entity is on
        if (entity == monsters.length) {
            entity = 0;
            manualAttack(0);
        } else {
            System.out.println(step);
            if (entity == 0) { // Initialises the very expressive options for reacting to monster attacks
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
    public void manualAttack(int o) {
        switch (step) {
            case 0:
                int p;
                // This code calculates how many tiles you can move to (options.length), and how far away the leftmost accessible tile is (p), which is used to calculate the relative position of each tile
                if (roomRadius - Math.abs(player.pos) < player.speed) {
                    if (roomRadius + Math.abs(player.pos) < player.speed) { // You can reach both sides of the room
                        options = new JButton[2*roomRadius/5 + 1];
                        p = - player.pos - roomRadius;
                    } else { // You can reach one side of the room
                        options = new JButton[2*roomRadius + 1 - Math.abs(player.pos)];
                        p = (player.pos > 0) ? player.pos - player.speed : - player.pos - roomRadius;
                    }
                } else { // You can't reach the sides of the room
                    options = new JButton[2*player.speed/5+1];
                    p = player.pos - player.speed;
                }
                for (int i = 0; i < options.length; i++) {
                    // i is the index, p is the relative position
                    for (int j = 0; j < monsters.length; j++) { // Checks every monster for each tile. Would be much faster to do it the other way round, but I'm lazy
                        if (player.pos + p == monsters[j].pos) { // If an enemy is on this tile, set
                            options[i] = new JButton(monsters[j].name + " (" + monsters[j].hp + "/" + monsters[j].maxHP + ")");
                            break;
                        }
                        if (j == monsters.length - 1) { // If no monsters are on this tile
                            int f = p; // must be final or semi-final
                            options[i] = new JButton(Math.abs(f) + "ft");
                            System.out.println(step);
                            options[i].addActionListener(l -> {
                                System.out.println(step);
                                step++;
                                manualAttack(f);
                            });
                        }
                    }
                    p += 5;
                }
                Window.infoPopup("Where will you move?", options);
                break;
            case 1: // Choosing which enemy to attack, fairly self-explanatory
                System.out.println("hi");
                player.pos += o;
                options = new JButton[monsters.length];
                for (int i = 0; i < options.length; i++) {
                    options[i] = new JButton(monsters[i].name + " (" + monsters[i].hp + "/" + monsters[i].maxHP + ")");
                    int f = i; // must be final or semi-final
                    options[i].addActionListener(l -> {
                        step++;
                        manualAttack(f);
                    });
                }
                Window.infoPopup("Which enemy will you attack?", options);
                break;
            case 2:
                battle();
                break;
        }

    }
    public void automaticAttack() {
        Entity enemy = monsters[entity];
        switch (step) {
            case 0:
                /*
                    The enemies are programmed to advance towards their target, and retreat when under half health.
                    The other parts of the code is about if they are close to a wall or their target, in which case they should be stopped.
                */
                int right = (enemy.pos > player.pos) ? 1 : -1;
                if (enemy.maxHP / enemy.hp < 2) {
                    // The enemy is moving towards you
                    int movementTarget = player.pos + 5 * right;
                    if (Math.abs(enemy.pos - movementTarget) > enemy.speed) {
                        // The enemy is too far away to reach you
                        enemy.pos -= enemy.speed * right;
                        Window.infoPopup("The enemy moved towards you (" + Math.abs(player.pos - enemy.pos) + "ft)", options);
                    } else {
                        // The enemy is close enough to reach you
                        enemy.pos = player.pos + 5 * right;
                        Window.infoPopup("The enemy moved next to you (5ft)", options);
                    }
                } else {
                    // The enemy is retreating
                    int movementTarget = roomRadius * right;
                    if (Math.abs(enemy.pos - movementTarget) > enemy.speed) {
                        // The enemy has space to retreat
                        enemy.pos += enemy.speed * right;
                        Window.infoPopup("The enemy moved away from you (" + Math.abs(player.pos - enemy.pos) + "ft)", options);
                    } else {
                        // The enemy is backed into a corner
                        enemy.pos = player.pos + 5 * right;
                        Window.infoPopup("The enemy moved against the wall (" + Math.abs(player.pos - enemy.pos) + "ft)", options);
                    }
                }
                break;
            case 1:
                Window.infoPopup("The enemy was too far away to attack", options); // This message gets wiped if a usable weapon is found
                for (Weapon w : enemy.weapons) {
                    if (w.canAttack(enemy.pos - player.pos)) {
                        int damage = w.attack(player.ac);
                        player.hp -= damage;
                        Window.infoPopup("The enemy attacked with their " + w.name + " and " + ((damage == 0) ? "missed" : "dealt " + damage + " damage"), options);
                        break;
                    }
                }
                break;
            case 2:
                if (player.hp > 0) { // If player is still alive
                    options[0].addActionListener(l -> {
                        entity++;
                        battle();
                    });
                    Window.infoPopup("You are now at " + player.hp + " health", options);
                } else { // If player is dead
                    options[0].addActionListener(l -> {
                        System.exit(0); // :(
                    });
                    Window.infoPopup("You have passed out", options);

                }
        }
    }
}
