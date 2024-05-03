package main;
import javax.swing.*;

import oop.entity.*;
import org.json.simple.*;

import java.awt.*;
import java.util.ArrayList;

public class Main {
    JFrame window;
    int currentRoom;
    int previousRoom;
    JSONArray rooms;
    boolean debug;
    Entity player = new Warrior();
    final int monsterTypes = 4; // Replace if you add more monsters
    public Main(JSONArray a){
        window = new JFrame();
        window.setVisible(true);
        window.toFront();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        rooms = a;
        newRoom();
    }
    private void newRoom() {
        window.getContentPane().removeAll();
        JSONObject room = (JSONObject) rooms.get(currentRoom);
        if (room.containsKey("monsters")) {
            occupiedRoom(room);
        } else {
            emptyRoom(room);
        }
    }
    private void occupiedRoom(JSONObject room) {
        ArrayList<String> monsterList = (ArrayList<String>) room.get("monsters");
        Entity[] monsters = new Entity[monsterList.size()];
        for (int i = 0; i < monsterList.size(); i++) {
            switch (monsterList.get(i)) {
                case "Giant Rat":
                    monsters[i] = new GiantRat();
                    break;
                case "Goblin":
                    monsters[i] = new Goblin();
                    break;
                case "Hobgoblin":
                    monsters[i] = new Hobgoblin();
                    break;
                case "Wolf":
                    monsters[i] = new Wolf();
                    break;
            }
        }
        if (monsterList.size() == 1) {
            window.add(new JLabel("There is 1 " + monsterList.get(0) + " in this room.", SwingConstants.CENTER));
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
            String text = "There " + ((monsterCount[0] == 1) ? "is " : "are ");
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
            window.add(new JLabel(text, SwingConstants.CENTER));
        }
        JPanel options = new JPanel();
        window.add(options);
        options.setLayout(new GridLayout());
        JButton retreat = new JButton("Retreat");
        options.add(retreat);
        retreat.addActionListener(l -> {
            currentRoom = previousRoom;
            newRoom();
        });
        JButton fight = new JButton("Fight");
        options.add(fight);
        fight.addActionListener(l -> {
            battle(room, monsters);

        });
        window.setLayout(new GridLayout(2,1));
        window.setSize(Math.max(options.getComponentCount() * 100, (int) window.getPreferredSize().getWidth() + 10), 200);
    }
    public void battle(JSONObject room, Entity[] monsters) {
        for (Entity m: monsters) {
            m.attack(window, player, Math.max((int) (((long) room.get("width"))), (int) (((long) room.get("height")))));
        }
    }
    private void emptyRoom(JSONObject room) {
        boolean explored = (boolean) room.put("explored", true);

        window.setTitle((room.containsKey("title"))
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
                window.add(tile[j][i]);
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

        window.setLayout(new GridLayout(height,width));
        window.setSize(width*100, height*100);
        window.revalidate();
        window.repaint();

    }
}