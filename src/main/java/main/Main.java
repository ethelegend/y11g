package main;
import javax.swing.*;

import org.json.simple.*;

import java.awt.*;

public class Main {
    JFrame window;
    int currentRoom = 0;
    JSONArray rooms;
    boolean debug = false;
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
            battle(room);
        } else {
            emptyRoom(room);
        }
    }
    private void battle(JSONObject room) {
        emptyRoom(room);
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