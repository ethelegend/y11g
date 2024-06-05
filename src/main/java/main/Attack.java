package main;

import oop.entity.Entity;
import oop.weapon.Weapon;

import javax.swing.JButton;

public class Attack {
    Entity player;
    Entity target;
    String targetTitle;
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
            target = monsters[entity];
            targetTitle = (monsters.length == 1) ? "The enemy" : "Enemy " + (entity + 1);
            automaticAttack();
        }
    }
    public void manualAttack(int o) {
        boolean canAttack;
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
                player.pos += o;
                options = new JButton[monsters.length];
                Weapon maxRange = null;
                for (Weapon w : player.weapons) {
                    if (maxRange == null || w.range > maxRange.range) {
                        maxRange = w;
                    }
                }
                canAttack = false;
                for (int i = 0; i < monsters.length; i++) {
                    assert maxRange != null;
                    if (maxRange.canAttack(monsters[i].pos)) {
                        canAttack = true;
                        options[i] = new JButton(monsters[i].name + " (" + monsters[i].hp + "/" + monsters[i].maxHP + ", " + Math.abs(player.pos - monsters[i].pos) + "ft)");
                        final int f = i; // must be final or semi-final
                        options[i].addActionListener(l -> manualAttack(f));
                    } else {
                        options[i] = new JButton(monsters[i].name + " (too far away)");
                    }
                }
                if (canAttack) {
                    Window.infoPopup("Which enemy will you attack?", options);
                } else {
                    options = new JButton[1];
                    options[0] = new JButton("OK");
                    options[0].addActionListener(l -> battle());
                    Window.infoPopup("You are too far away to attack", options);
                }

                break;
            case 3:
                target = monsters[o];
                options = new JButton[player.weapons.length];
                canAttack = false;
                for (int i = 0; i < options.length; i++) {
                    if (player.weapons[i].canAttack(player.pos - target.pos)) {
                        canAttack = true;
                        options[i] = new JButton(player.weapons[i].name);
                        final int f = i; // must be final or semi-final
                        options[i].addActionListener(l -> manualAttack(player.weapons[f].attack(target.ac)));
                    } else {
                        options[i] = new JButton(player.weapons[i].name + " (Too far away)");
                    }
                }
                if (canAttack) {
                    Window.infoPopup("What weapon will you use?", options);
                } else {
                    options = new JButton[1];
                    options[0] = new JButton("OK");
                    options[0].addActionListener(l -> battle());
                    Window.infoPopup("You are too far away to attack", options);
                }
                break;
            case 4:
                options = new JButton[1];
                options[0] = new JButton("OK");
                if (o == 0) {
                    options[0].addActionListener(l -> battle());
                    Window.infoPopup("Your attack missed", options);
                } else {
                    if (target.hp <= o) {
                        target.hp = 0;
                        options[0].addActionListener(l -> manualAttack(0));
                    } else {
                        target.hp -= o;
                        options[0].addActionListener(l -> battle());

                    }
                    Window.infoPopup("You dealt " + o + " damage (" + target.hp + "/" + target.maxHP + ")", options);

                }
                break;
            case 5:
                entity = -1; // IDK why i have to do this but i have to
                int gold = -1;
                if (monsters.length > 1) {
                    Entity[] monstersTemp = new Entity[monsters.length - 1];
                    for (int i = 0; i < monstersTemp.length; i++) {
                        if (gold < 0 & monsters[i].hp == 0) {
                            gold = monsters[i].hp;
                        }
                        if (gold >= 0) {
                            monstersTemp[i] = monsters[i+1];
                            monsters[i+1] = null;
                        } else {
                            monstersTemp[i] = monsters[i];
                        }
                    }
                    monsters = monstersTemp;
                    options[0].addActionListener(l -> battle());
                    Window.infoPopup("You defeated the enemy" + ((gold > 0) ? " and earned " + gold + " gold" :""), options);
                } else {
                    gold = monsters[0].gold;
                    options[0] = new JButton("OK");
                    options[0].addActionListener(l -> origin.newRoom());
                    Window.infoPopup("You defeated all enemies" + ((gold > 0) ? " and earned " + gold + " gold" :""), options);
                }
                player.gold += gold;
                break;
        }

    }
    public void automaticAttack() {
        step++;
        switch (step) {
            case 1:
                /*
                    The enemies are programmed to advance towards their target, and retreat when at or under half health.
                    The other parts of the code is about if they are close to a wall or their target, in which case they should be stopped.
                */
                int right = (target.pos > player.pos) ? 1 : -1;
                if (target.maxHP / target.hp < 2) { // I have to do it this way or cast them to floats
                    // The enemy is moving towards you
                    if (target.canMove(player.pos + 5 * right)) {
                        // The enemy is close enough to reach you
                        target.pos = player.pos + 5 * right;
                        Window.infoPopup(targetTitle + " moved next to you (5ft)", options);
                    } else {
                        // The enemy is too far away to reach you
                        target.pos -= target.speed * right;
                        Window.infoPopup(targetTitle + " moved towards you (" + Math.abs(player.pos - target.pos) + "ft)", options);
                    }
                } else {
                    // The enemy is retreating
                    if (target.canMove(roomRadius * right)) {
                        // The enemy is backed into a corner
                        target.pos = roomRadius * right;
                        Window.infoPopup(targetTitle + " moved against the wall (" + Math.abs(player.pos - target.pos) + "ft)", options);
                    } else {
                        // The enemy has space to retreat
                        target.pos += target.speed * right;
                        Window.infoPopup(targetTitle + " moved away from you (" + Math.abs(player.pos - target.pos) + "ft)", options);
                    }
                }
                break;
            case 2:
                 Weapon chosen = null;
                for (Weapon w : target.weapons) {
                    if (w.canAttack(target.pos - player.pos)) {
                        chosen = w;
                    } else {
                        break;
                    }
                }
                if (chosen == null) {
                    Window.infoPopup(targetTitle + " was too far away to attack", options);
                } else {
                    int damage = chosen.attack(player.ac);
                    player.hp -= damage;
                    Window.infoPopup(targetTitle + " attacked with their " + chosen.name + " and " + ((damage == 0) ? "missed" : "dealt " + damage + " damage"), options);
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
