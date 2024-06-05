package main;
// GUI elements
import javax.swing.*;
import java.awt.*;

import oop.entity.*; // Entity classes
// JSON objects
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList; // This is used once

public class Main {
    int currentRoom = 16;
    int previousRoom; // Used for retreating from occupied rooms
    JSONArray rooms;
    boolean debug; // Shows room numbers by default if true
    Entity player = new Warrior();
    final int monsterTypes = 4; // Should probably replace this with an enum or something
    public Main(JSONArray a){
        new Window(); // Initialises the JFrame
        rooms = a; // Initialises the JSONArray
        newRoom();
    }
    public void newRoom() {
        JSONObject room = (JSONObject) rooms.get(currentRoom); // Gets the current room
        if (room.containsKey("monsters")) {
            occupiedRoom(room);
        } else {
            emptyRoom(room);
        }
    }
    private void occupiedRoom(JSONObject room) {
        Entity[] monsters = new Entity[((JSONArray) room.get("monsters")).size()]; // Array of monsters
        ArrayList<String> monsterList = new ArrayList<String>(); // ArrayList of monster names

        for (int i = 0; i < monsters.length; i++) { // Copies monster JSONObjects to the array and arrayList
            JSONObject m = (JSONObject) ((JSONArray) room.get("monsters")).get(i);
            monsterList.add((String) m.get("type"));
            switch ((String) m.get("type")) { // This could definitely be done better
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
        String text; // The text displayed telling you how many enemies are in the room
        if (monsters.length == 1) { // Easy case
            text = "There is 1 " + monsterList.get(0) + " in this room.";
        } else {
            int[] monsterCount = new int[monsterTypes]; // monsterCount[x] tells you how many of monsterList.get(x) there are
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
            text = "There " + ((monsterCount[0] == 1) ? "is " : "are "); // There is / there are
            for (int i = 0; i < monsterList.size(); i++) {
                String s = monsterCount[i] + " " + monsterList.get(i) + ((monsterCount[i] == 1) ? " " : "s "); // "2" + " " + "Goblin" + "s "
                text = text.concat(s);
                System.out.println(text);

                if (monsterCount[i+1] == 0) {
                    text = text.concat("in this room.");
                } else {
                    if (monsterList.size() > 2) {
                        text = text.concat(", ");
                    }
                    if (monsterCount[i+2] == 0) {
                        text = text.concat("and ");
                    }
                }
            }
        }
        JButton retreat = new JButton("Retreat");
        retreat.addActionListener(l -> {
            currentRoom = previousRoom;
            newRoom();
        });
        JButton fight = new JButton("Fight");
        fight.addActionListener(l -> {
            room.remove("monsters");
            new Attack(player, monsters, Math.max((int) ((long) room.get("width")), (int) ((long) room.get("height")))/2*5, this);
        });
        Window.infoPopup(text, new JButton[]{retreat, fight});
    }
    private void emptyRoom(JSONObject room) {
        Window.window.getContentPane().removeAll();
        boolean explored = (boolean) room.put("explored", true); // It does what I need it to, even if it is cursed
        // Sets the title of the room
        Window.window.setTitle((room.containsKey("title"))
                ? (String) room.get("title")
                : ((debug || explored)
                ? Integer.toString(currentRoom)
                : ""));
        // Gets the width and height of the room
        int width = ((Long) room.get("width")).intValue();
        int height = ((Long) room.get("height")).intValue();

        JButton[][] tile = new JButton[width][height]; // Multidimensional arrays :)
        for (int i = 0; i < height; i++) { // Initialises each JButton and adds it to the JFrame
            for (int j = 0; j < width; j++) {
                tile[j][i] = new JButton();
                Window.window.add(tile[j][i]);
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
                currentRoom = (int) ((long) j.get("to"));
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

        Window.window.setLayout(new GridLayout(height,width)); // Makes each button's position on the screen equivalent to its indices
        Window.window.setSize(width*100, height*100); // Each button is 100px*100px
        // The magic functions that make sure it renders correctly
        Window.window.revalidate();
        Window.window.repaint();

    }
}