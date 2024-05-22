package main;
import javax.swing.*;

import oop.entity.*;
import org.json.simple.*;

import java.awt.*;
import java.util.ArrayList;

public class Main {
    int currentRoom = 16;
    int previousRoom;
    JSONArray rooms;
    boolean debug;
    Entity player = new Warrior();
    final int monsterTypes = 4; // Replace if you add more monsters
    public Main(JSONArray a){
        new Window();
        rooms = a;
        newRoom();
    }
    private void newRoom() {
        JSONObject room = (JSONObject) rooms.get(currentRoom);
        if (room.containsKey("monsters")) {
            occupiedRoom(room);
        } else {
            emptyRoom(room);
        }
    }
    private void occupiedRoom(JSONObject room) {
        Entity[] monsters = new Entity[((JSONArray) room.get("monsters")).size()];
        ArrayList<String> monsterList = new ArrayList<String>();

        for (int i = 0; i < monsters.length; i++) {
            JSONObject m = (JSONObject) ((JSONArray) room.get("monsters")).get(i);
            monsterList.add((String) m.get("type"));
            switch ((String) m.get("type")) {
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
        String text;
        if (monsters.length == 1) {
            text = "There is 1 " + monsterList.get(0) + " in this room.";
        } else {
            int[] monsterCount = new int[monsterTypes];
            for (int i = 0; i < monsterTypes; i++) {
                if (i == monsterList.size()) {break;}
                monsterCount[i]++;
                for (int j = i + 1; j < monsterList.size(); j++) {
                    if (monsterList.get(i).equals(monsterList.get(j))) {
                        monsterCount[i]++;
                        monsterList.remove(j);
                        j--;
                    }
                }
            }
            text = "There " + ((monsterCount[0] == 1) ? "is " : "are ");
            for (int i = 0; i < monsterList.size(); i++) {
                String s = monsterCount[i] + " " + monsterList.get(i) + ((monsterCount[i] == 1) ? " " : "s ");
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
            battle(room, monsters);

        });
        Window.infoPopup(text, new JButton[]{retreat, fight});
    }
    public void battle(JSONObject room, Entity[] monsters) {
        for (Entity m: monsters) {
            new Attack(player, monsters, Math.max((int) ((long) room.get("width")), (int) ((long) room.get("height")))/2);
        }
    }
    private void emptyRoom(JSONObject room) {
        Window.window.getContentPane().removeAll();
        boolean explored = (boolean) room.put("explored", true);

        Window.window.setTitle((room.containsKey("title"))
                ? (String) room.get("title")
                : ((debug || explored)
                ? Integer.toString(currentRoom)
                : ""));

        int width = ((Long) room.get("width")).intValue();
        int height = ((Long) room.get("height")).intValue();

        JButton[][] tile = new JButton[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                tile[j][i] = new JButton();
                Window.window.add(tile[j][i]);
            }
        }
        JSONArray exits = (JSONArray) room.get("exits");
        for (Object o : exits) {
            JSONObject j = (JSONObject) o;
            int x = ((Long) j.get("x")).intValue();
            int y = ((Long) j.get("y")).intValue();
            tile[x][y].setText((debug || (boolean) ((JSONObject) rooms.get(((Long) j.get("to")).intValue())).get("explored"))
                    ? Long.toString((Long) j.get("to"))
                    : (String) j.get("label"));
            tile[x][y].addActionListener(l -> {
                previousRoom = currentRoom;
                currentRoom = ((Long) j.get("to")).intValue();
                newRoom();
            });
        }
        if (currentRoom == 0) {
            tile[0][0].setText("Debug");
            tile[0][0].addActionListener(l -> {
                debug = !debug;
                newRoom();
            });
        }

        Window.window.setLayout(new GridLayout(height,width));
        Window.window.setSize(width*100, height*100);
        Window.window.revalidate();
        Window.window.repaint();

    }
}