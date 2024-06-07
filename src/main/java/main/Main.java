/*
    This class handles the main functions of the game.
    It lets you enter a room, displays it and the exits, and prompts you to go to Attack if there are enemies
    Its code structure is that every time a room changes, newRoom() gets called, and it calls emptyRoom() or occupiedRoom().
*/

package main;

// GUI elements
import javax.swing.*;
import java.awt.GridLayout;

// Entity classes
import oop.entity.Entity;
import oop.entity.GiantRat;
import oop.entity.Goblin;
import oop.entity.Hobgoblin;
import oop.entity.Wolf;

// JSON objects
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

// Window and player objects

// This is used once because i need the one line element removal and didnt want to have to code it
import java.util.ArrayList;

public class Main {
    int currentRoom = 0;
    int previousRoom; // Used for retreating from occupied rooms
    JSONArray rooms; // The entirety of the dungeon is stored in this format
    boolean debug; // Shows room numbers by default if true, when false you have to have visited/cleared the room before
    final int monsterTypes = 4; // Should probably replace this with an enum or something but im kinda running out of time to learn how to do it
    public Main(JSONArray a){
        rooms = a; // Saves the JSONArray
        newRoom();
    }
    public void newRoom() { // Very self explanatory
        JSONObject room = (JSONObject) rooms.get(currentRoom);
        emptyRoom(room);
        if (room.containsKey("monsters")) {
            occupiedRoom(room);
        } else {
            emptyRoom(room);
        }
    }
    private void emptyRoom(JSONObject room) { // When there are no monsters
        Static.window.getContentPane().removeAll();
        boolean explored = (boolean) room.put("explored", true); // It does what I need it to, even if IntelliJ doesnt like how unsanitised it is
        // Sets the title of the room
        Static.window.setTitle((room.containsKey("title"))
                ? (String) room.get("title")
                : ((debug || explored)
                        ? Integer.toString(currentRoom)
                        : ""));
        // Gets the width and height of the room. If i didnt cast it to a long beforehand, java would throw an error because JSONObject stores it as a long
        int width = (int) ((long) room.get("width"));
        int height = (int) ((long) room.get("height"));

        JButton[][] tile = new JButton[width][height]; // Multidimensional arrays :)
        for (int i = 0; i < height; i++) { // Initialises each JButton and adds it to the JFrame. Since the amount of buttons per row is limited to (width), this order of adding buttons makes the indexes line up with its coordinates
            for (int j = 0; j < width; j++) {
                tile[j][i] = new JButton();
                Static.window.add(tile[j][i]);
            }
        }
        JSONArray exits = (JSONArray) room.get("exits"); // Gets all the exits
        for (Object o : exits) { // I can't typecast inside a foreach, so I have to use a generic object for now
            JSONObject j = (JSONObject) o;
            // Gets the x pos and y pos
            int x = (int) ((long) j.get("x"));
            int y = (int) ((long) j.get("y"));
            tile[x][y].setText((debug || (boolean) ((JSONObject) rooms.get((int) ((long) j.get("to")))).get("explored")) // If debug is enabled or the room has been explored
                    ? Long.toString((long) j.get("to"))
                    : (String) j.get("label"));
            tile[x][y].addActionListener(l -> { // Lambda that moves you to a new room
                previousRoom = currentRoom;
                currentRoom = (int) ((long) j.get("to")); // MOVE FORWARDS / BACKWARDS
                newRoom();
            });
        }
        if (currentRoom == 0) { // Manually sets one specific tile as the debug toggle tile
            tile[0][0].setText("Debug");
            tile[0][0].addActionListener(l -> {
                debug = !debug;
                newRoom();
            });
        }

        Static.window.setLayout(new GridLayout(height,width)); // Makes each button's position on the screen equivalent to its indices
        Static.window.setJMenuBar(Static.menu); // Reapplies the quit button
        Static.window.setSize(width*100, height*100+Static.menu.getHeight()); // Each button is 100px*100px. Doesnt fully work in room 0 for some reason but it doesnt really matter

    }
    private void occupiedRoom(JSONObject room) {
        Entity[] monsters = new Entity[((JSONArray) room.get("monsters")).size()]; // Array of monster objects
        ArrayList<String> monsterList = new ArrayList<>(); // ArrayList of monster names

        for (int i = 0; i < monsters.length; i++) { // Copies monster JSONObjects to the array and arrayList
            JSONObject m = (JSONObject) ((JSONArray) room.get("monsters")).get(i);
            monsterList.add((String) m.get("type"));
            switch ((String) m.get("type")) { // See line 26
                case "Giant Rat":
                    monsters[i] = new GiantRat((int)((long) m.get("pos")), (int)((long) m.get("gold")));
                    break;
                case "Goblin":
                    monsters[i] = new Goblin((int)((long) m.get("pos")), (int)((long) m.get("gold")));
                    break;
                case "Hobgoblin":
                    monsters[i] = new Hobgoblin((int)((long) m.get("pos")), (int)((long) m.get("gold")));
                    break;
                case "Wolf":
                    monsters[i] = new Wolf((int)((long) m.get("pos")), (int)((long) m.get("gold")));
                    break;
            }
        }
        String text; // The text displayed telling you how many enemies are in the room. This is the main reason why this function is 67 lines long. Sorry in advance
        if (monsters.length == 1) { // Trivial case
            text = "There is 1 " + monsterList.get(0) + " in this room.";
        } else {
            // Creates a new variable monsterCount. Alters monsterCount and monsterList so that there are no repeats in new monsterList and for the string at new monsterList.get(x), monsterCount[x] lists the amount of copies in the old monsterList.
            int[] monsterCount = new int[monsterTypes];
            for (int i = 0; i < monsterTypes; i++) { // For each kind of monster
                if (i == monsterList.size()) {break;} // If there are no more kinds of monster in this room
                // Checks the type of monster at monsterList.get(i), for each additional instance of that type, increments monsterCount and removes the instance from monsterList. This guarantees that the monsterList is converted into a list of each unique type with monsterCount conveying how many there are
                monsterCount[i] = 1;
                for (int j = i + 1; j < monsterList.size(); j++) {
                    if (monsterList.get(i).equals(monsterList.get(j))) {
                        monsterCount[i]++;
                        monsterList.remove(j);
                        j--;
                    }
                }
            }
            text = "There " + ((monsterCount[0] == 1) ? "is " : "are "); // "There " + "are "
            for (int i = 0; i < monsterList.size(); i++) {
                String s = monsterCount[i] + " " + monsterList.get(i) + ((monsterCount[i] == 1) ? " " : "s "); // "2" + " " + "Goblin" + "s "
                text = text.concat(s);

                if (monsterCount[i+1] == 0) { // That was the last type of monster
                    text = text.concat("in this room.");
                } else {
                    if (monsterCount[i+2] == 0) { // That was the 2nd to last type of monster
                        text = text.concat("and ");
                    }
                    if (monsterList.size() > 2) { // There are more than 2 types of monster. replace with else if if you have an incorrect opinion about oxford's comma
                        text = text.concat(", ");
                    }
                }
            }
        }
        JButton retreat = new JButton("Retreat"); // I am a coward
        retreat.addActionListener(l -> {
            currentRoom = previousRoom;
            newRoom();
        });
        JButton fight = new JButton("Fight"); // I am a fool
        fight.addActionListener(l -> {
            room.remove("monsters"); // If you die, you reset and it doesn't matter. If you win, it calls newRoom and will call occupiedRoom if this key exists
            new Attack(monsters, Math.max((int) ((long) room.get("width")), (int) ((long) room.get("height")))/2*5, this);
        });
        Static.infoPopup(text, new JButton[]{retreat, fight});
    }
}