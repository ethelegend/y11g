/*
    This class handles battle logic. Apologies in advance for anyone trying to read this
    It loops through each entity in the battle and either automatically makes moves or prompts the user for instructions.
    The main structure of this file is a battle() class that initially calls the attack() classes for each enemy, and those classes rerun with different instructions due to a switch and a global index.
*/

package main;

import oop.entity.Entity;
import oop.weapon.Weapon;

import javax.swing.JButton;
import javax.swing.JLabel;

public class Attack {
    Entity target; // removing this would mostly be fine, but some uses of this would require some refactoring
    String targetTitle; // "The enemy " if only 1 enemy, "Enemy # " if more than 1
    Entity[] monsters;
    int roomRadius; // Would be more time-consuming to make a 2-dimensional arena, and 1d works well
    int entity; // Index 1 for which entity is moving, monsters.length -> player, < monsters.length -> monsters[entity]. Shifted down by one because it is incremented every time battle() is called
    int step; // Index 2 for which step of the attack the entity is on
    JButton[] options;
    Main origin;

    public Attack(Entity[] m, int r, Main o) {
        monsters = m; // Array of monster objects
        roomRadius = r; // Radius of room (e.g. r = 10: {-10, -5, 0, 5, 10}
        Static.player.pos = -r; // The player enters the room
        entity = monsters.length - 1;
        origin = o;
        battle(); // we love recursion
    }

    public void battle() {
        step = 0;
        entity++;

        if (entity == monsters.length) {
            entity = -1; // Next time, entity = 0
            manualAttack(0);
        } else {

            if (entity == 0) { // Initialises the very expressive options for reacting to monster attacks
                System.out.println(step);
                options = new JButton[1];
                options[0] = new JButton("OK");
                options[0].addActionListener(l -> automaticAttack());
            }

            target = monsters[entity];
            targetTitle = (monsters.length == 1) ? "The enemy" : "Enemy " + (entity + 1);
            automaticAttack();
        }
    }
    public void manualAttack(int o) { // Which button you pressed. Specific format depends on which step you are on
        step++;
        switch (step) {
            case 1: // Choosing where to move
                options = new JButton[2 * roomRadius / 5 + 1];
                int p = -Static.player.pos - roomRadius; // Position of each tile relative to the player position. Stays synchronised because it is incremented by 5ft each loop

                for (int i = 0; i < options.length; i++) {
                    // i is the index, p is the relative position
                    if (Static.player.canMove(Static.player.pos + p)) { // If the position is reachable
                        options[i] = new JButton(Math.abs(p) + "ft"); // Write distance
                        final int f = p; // To have a variable in a lambda it has to be final or semi-final, so this is necessary for these and other buttons
                        options[i].addActionListener(l -> manualAttack(f));
                    } else {
                        options[i] = new JButton(); // Blank button to indicate unmoveability
                    }
                    p += 5;
                }

                for (Entity monster : monsters) { // Displays where the monsters are. If a monster doesn't show up, they are on the same tile as another monster, which would take a lot of refactoring to patch
                    options[(monster.pos + roomRadius) / 5] = new JButton(monster.name + " (" + monster.hp + "/" + monster.maxHP + ")"); // This has the benefits of setting the text and removing the actionListener, making it impossible to move to an occupied tile
                }

                Static.infoPopup("Where will you move?", options);
                break;

            case 2: // Choosing what to attack
                Static.player.pos += o; // Moves the player to the tile they selected
                options = new JButton[monsters.length];

                // Finds the weapon with the maximum range
                Weapon maxRange = null;
                for (Weapon w : Static.player.weapons) {
                    if (maxRange == null || w.range > maxRange.range) {
                        maxRange = w;
                    }
                }

                boolean canAttack = false; // If this is false after the for loop, no enemies are in range and you need to skip your turn
                for (int i = 0; i < monsters.length; i++) {
                    assert maxRange != null; // The first iteration of the foreach loop must set a value for maxRange
                    if (maxRange.canAttack(monsters[i].pos)) { // If enemy in range
                        canAttack = true;
                        options[i] = new JButton(monsters[i].name + " (" + monsters[i].hp + "/" + monsters[i].maxHP + ", " + Math.abs(Static.player.pos - monsters[i].pos) + "ft)"); // Displays name, health, and relative position
                        final int f = i;
                        options[i].addActionListener(l -> manualAttack(f));
                    } else {
                        options[i] = new JButton(monsters[i].name + " (too far away)");
                    }
                }

                if (canAttack) {
                    Static.infoPopup("Which enemy will you attack?", options); // Business as usual
                } else { // Can't attack, need new options
                    options = new JButton[1];
                    options[0] = new JButton("OK");
                    options[0].addActionListener(l -> battle());
                    Static.infoPopup("You are too far away to attack", options);
                }
                break;

            case 3: // Choosing which weapon to attack with
                target = monsters[o];
                options = new JButton[Static.player.weapons.length];

                for (int i = 0; i < options.length; i++) { // For each of the player's weapons
                    if (Static.player.weapons[i].canAttack(Static.player.pos - target.pos)) { // If the target is in range
                        options[i] = new JButton(Static.player.weapons[i].name);
                        final int f = i;
                        options[i].addActionListener(l -> manualAttack(Static.player.weapons[f].attack(target.ac)));
                    } else {
                        options[i] = new JButton(Static.player.weapons[i].name + " (Too far away)"); // No need to check if any weapons are in range because last step required you to pick an enemy in range of at least 1 of your weapons
                    }
                }

                Static.infoPopup("What weapon will you use?", options);
                break;

            case 4:
                // Out of your hands
                options = new JButton[1];
                options[0] = new JButton("OK");
                options[0].addActionListener(l -> battle());

                if (o == 0) { // The attack function returns 0 for a missed attack, because a successful attack always does at least 1 damage
                    Static.infoPopup("Your attack missed", options);

                } else {
                    if (target.hp > o) { // If the enemy can withstand the attack
                        target.hp -= o;

                    } else {
                        target.hp = 0; // Since target is a reference to an element in monsters, this allows me to find the index of the monster
                        Static.player.gold += target.gold; // PICKUP

                        if (monsters.length > 1) {

                            // If there are more monsters, shrink the array (I can't use arrayList because i can't cast it back from Object to Entity for some reason)
                            Entity[] monstersTemp = new Entity[monsters.length - 1]; // Temp array
                            for (int i = 0; i < monstersTemp.length; i++) { // This will copy element i until it finds the dead monster, then starts copying i+1, deleting it afterwards to tell the next iteration to continue
                                if (monsters[i] == null || monsters[i].hp == 0) {
                                    monstersTemp[i] = monsters[i + 1];
                                    monsters[i + 1] = null;
                                } else {
                                    monstersTemp[i] = monsters[i];
                                }
                            }
                            monsters = monstersTemp;

                        } else { // You defeated all the enemies
                            options[0] = new JButton("OK");
                            options[0].addActionListener(l -> origin.newRoom());
                        }
                    }
                    Static.infoPopup("You dealt " + o + " damage (" + target.hp + "/" + target.maxHP + ((target.hp == 0 && target.gold > 0) ? ") and earned " + target.gold + "gold" : ")"), options);
                }
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
                int right = (target.pos > Static.player.pos) ? 1 : -1;

                if (target.maxHP / target.hp < 2) { // I have to do it this way or cast them to floats
                    // The enemy is moving towards you
                    if (target.canMove(Static.player.pos + 5 * right)) {
                        System.out.println("why");
                        // The enemy is close enough to reach you
                        target.pos = Static.player.pos + 5 * right;
                        Static.infoPopup(targetTitle + " moved next to you (5ft)", options);

                    } else {
                        // The enemy is too far away to reach you
                        target.pos -= target.speed * right;
                        Static.infoPopup(targetTitle + " moved towards you (" + Math.abs(Static.player.pos - target.pos) + "ft)", options);
                    }

                } else {
                    // The enemy is retreating
                    if (target.canMove(roomRadius * right)) {
                        // The enemy is backed into a corner
                        target.pos = roomRadius * right;
                        Static.infoPopup(targetTitle + " moved against the wall (" + Math.abs(Static.player.pos - target.pos) + "ft)", options);

                    } else {
                        // The enemy has space to retreat
                        target.pos += target.speed * right;
                        Static.infoPopup(targetTitle + " moved away from you (" + Math.abs(Static.player.pos - target.pos) + "ft)", options);
                    }
                }
                break;

            case 2:
                // Since weapons are added from largest to smallest range, this gets the weapon with the smallest range that can attack, which is usually the strongest valid weapon
                Weapon chosen = null;
                for (Weapon w : target.weapons) {
                    if (w.canAttack(target.pos - Static.player.pos)) {
                        chosen = w;
                    } else {
                        break;
                    }
                }

                if (chosen == null) {
                    Static.infoPopup(targetTitle + " was too far away to attack", options); // Out of range

                } else {
                    int damage = chosen.attack(Static.player.ac); // Attack
                    Static.player.hp -= damage; // Damage
                    Static.infoPopup(targetTitle + " used their " + chosen.name.toLowerCase() + " and " + ((damage == 0) ? "missed" : "dealt " + damage + " damage"), options); // Message
                }
                break;

            case 3:
                if (Static.player.hp > 0) { // If player is still alive
                    Static.infoPopup("You are at " + Static.player.hp + " health", options);

                } else { // If player is dead :(
                    Static.window.removeAll();
                    Static.window.setJMenuBar(Static.menu); // No button for you, only menu
                    Static.window.add(new JLabel("You have passed out"));
                    Static.window.setSize(100,100+Static.menu.getHeight());
                }
                break;

            case 4:
                battle(); // I don't change the actionListener because it stays the same for all monsters' turns, letting me only change it after a player turn
                break;
        }
    }
}