package main;

import oop.entity.Entity;
import oop.weapon.Weapon;

import javax.swing.*;

public class Attack {
    Entity player;
    Entity target;
    Entity[] monsters;
    int roomRadius;
    int entity;
    int step;
    JButton[] options;
    Main origin;
    public Attack(Entity p, Entity[] m, int r, Main o) {
        player = p; // The player object
        monsters = m; // Array of monster objects
        roomRadius = r; // Radius of room (e.g. r = 10: {-10, -5, 0, 5, 10}
        player.pos = -r; // The player enters the room
        entity = monsters.length - 1; // An index for which entity is moving, monsters.length -> player, < monsters.length -> monsters[entity]. Shifted down by one because it is incremented every time battle() is called
        origin = o;
        battle(); // we love recursion
    }

    public void battle() {
        step = 0; // 2nd index for which step of the attack the entity is on
        entity++;
        if (entity == monsters.length) {
            entity = -1;
            manualAttack(0);
        } else {
            if (entity == 0) { // Initialises the very expressive options for reacting to monster attacks
                options = new JButton[1];
                options[0] = new JButton("OK");
                options[0].addActionListener(l -> automaticAttack());
            }
            automaticAttack();
        }
    }
    public void manualAttack(int o) {
        step++;
        switch (step) {
            case 1:
                options = new JButton[2*roomRadius/5 + 1];
                int p = -player.pos - roomRadius;
                for (int i = 0; i < options.length; i++) {
                    // i is the index, p is the relative position
                    if (player.canMove(p)) {
                        options[i] = new JButton(Math.abs(p) + "ft");
                        final int f = p;
                        options[i].addActionListener(l -> manualAttack(f));
                    } else {
                        options[i] = new JButton();
                    }
                    p+=5;
                }
                for (int i = 0; i < monsters.length; i++) {
                    options[(monsters[i].pos + roomRadius)/5] = new JButton(monsters[i].name + " (" + monsters[i].hp + "/" + monsters[i].maxHP + ")");
                }
                Window.infoPopup("Where will you move?", options);
                break;
            case 2: // Choosing which enemy to attack, fairly self-explanatory
                System.out.println("hi");
                player.pos += o;
                options = new JButton[monsters.length + 1];
                for (int i = 0; i < monsters.length; i++) {
                    if (player.weapons[player.weapons.length - 1].canAttack(monsters[i].pos)) {
                        options[i] = new JButton(monsters[i].name + " (" + monsters[i].hp + "/" + monsters[i].maxHP + ", " + Math.abs(player.pos - monsters[i].pos) + "ft)");
                        final int f = i; // must be final or semi-final
                        options[i].addActionListener(l -> {
                            manualAttack(f);
                        });
                    } else {
                        options[i] = new JButton(monsters[i].name + " (too far away)");
                    }
                    options[monsters.length] = new JButton("Skip turn");
                    options[monsters.length].addActionListener(l -> battle());
                }
                Window.infoPopup("Which enemy will you attack?", options);
                break;
            case 3:
                target = monsters[o];
                System.out.println("bye");
                int c;
                for (c = 1; c < monsters.length; c++) {
                    if (player.weapons[player.weapons.length - c].canAttack(player.pos - monsters[o].pos)) {
                        break;
                    }
                }
                System.out.println(c);
                if (c == 0) {
                    options = new JButton[1];
                    options[0] = new JButton("OK");
                    options[0].addActionListener(l -> battle());
                    Window.infoPopup("You are too far away to attack", options);
                } else {
                    options = new JButton[c];
                    for (int i = 0; i < options.length; i++) {
                        options[i] = new JButton(player.weapons[i].name);
                        final int f = i; // must be final or semi-final
                        options[i].addActionListener(l -> {
                            manualAttack(player.weapons[f].attack(target.ac));
                        });
                    }
                    Window.infoPopup("What weapon will you use?", options);
                }
                break;
            case 4:

            case 5:
                options = new JButton[1];
                options[0] = new JButton("OK");
                if (o == 0) {
                    options[0].addActionListener(l -> {
                        battle();
                    });
                    Window.infoPopup("Your attack missed", options);
                } else {
                    target.hp -= Math.min(o, target.hp);
                    if (target.hp == 0) {
                        options[0].addActionListener(l -> {
                            manualAttack(0);
                        });
                    } else {
                        options[0].addActionListener(l -> {
                            battle();
                        });

                    }

                    Window.infoPopup("You dealt " + o + " damage (" + target.hp + "/" + target.maxHP + ")", options);
                }
            case 6:
                if (monsters.length > 1) {
                    Entity[] monstersTemp = new Entity[monsters.length - 1];
                    for (int i = 0; i < monstersTemp.length; i++) {
                        if (monsters[i] == null | monsters[i].hp <= 0) {
                            monstersTemp[i] = monsters[i+1];
                            monsters[i+1] = null;
                        } else {
                            monstersTemp[i] = monsters[i];
                        }
                    }
                    monsters = monstersTemp;
                    Window.infoPopup("You defeated the enemy", options);
                } else {
                    options[0] = new JButton("OK");
                    options[0].addActionListener(l -> origin.newRoom());
                    Window.infoPopup("You defeated all enemies", options);
                }
                options[0].addActionListener(l -> battle());
                break;
        }

    }
    public void automaticAttack() {
        target = monsters[entity];
        step++;
        switch (step) {
            case 1:
                /*
                    The enemies are programmed to advance towards their target, and retreat when under half health.
                    The other parts of the code is about if they are close to a wall or their target, in which case they should be stopped.
                */
                int right = (target.pos > player.pos) ? 1 : -1;
                if (target.maxHP / target.hp < 2) {
                    // The enemy is moving towards you
                    int movementTarget = player.pos + 5 * right;
                    if (target.canMove(movementTarget)) {
                        // The enemy is too far away to reach you
                        target.pos -= target.speed * right;
                        Window.infoPopup("The enemy moved towards you (" + Math.abs(player.pos - target.pos) + "ft)", options);
                    } else {
                        // The enemy is close enough to reach you
                        target.pos = player.pos + 5 * right;
                        Window.infoPopup("The enemy moved next to you (5ft)", options);
                    }
                } else {
                    // The enemy is retreating
                    int movementTarget = roomRadius * right;
                    if (target.canMove(movementTarget)) {
                        // The enemy has space to retreat
                        target.pos += target.speed * right;
                        Window.infoPopup("The enemy moved away from you (" + Math.abs(player.pos - target.pos) + "ft)", options);
                    } else {
                        // The enemy is backed into a corner
                        target.pos = player.pos + 5 * right;
                        Window.infoPopup("The enemy moved against the wall (" + Math.abs(player.pos - target.pos) + "ft)", options);
                    }
                }
                break;
            case 2:
                Window.infoPopup("The enemy was too far away to attack", options); // This message gets wiped if a usable weapon is found
                for (Weapon w : target.weapons) {
                    if (w.canAttack(target.pos - player.pos)) {
                        int damage = w.attack(player.ac);
                        player.hp -= damage;
                        Window.infoPopup("The enemy attacked with their " + w.name + " and " + ((damage == 0) ? "missed" : "dealt " + damage + " damage"), options);
                        break;
                    }
                }
                break;
            case 3:
                if (player.hp > 0) { // If player is still alive
                    Window.infoPopup("You are now at " + player.hp + " health", options);
                } else { // If player is dead :(
                    options[0].addActionListener(l -> System.exit(0));
                    Window.infoPopup("You have passed out", options);

                }
                break;
            case 4:
                battle();
                break;
        }
    }
}
