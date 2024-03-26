package main;
import javax.swing.*;

import org.json.simple.*;

import java.awt.*;

public class Main {
    JFrame window;
    int width;
    int height;
    JButton[][] tile;
    int currentRoom = 0;
    JSONObject room;
    final JSONArray rooms;
    boolean debug = false;
    public Main(JSONArray a){
        window = new JFrame();
        window.setVisible(true);
        window.toFront();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        rooms = a;
        room = (JSONObject) rooms.get(currentRoom);
        renderer(room);
    }
    private void renderer(JSONObject room) {
        window.getContentPane().removeAll();
        if (room.containsKey("title")) {

        } else if (debug) {

        } else {

        }
        window.setTitle((room.containsKey("title"))
                ? (String) room.get("title")
                : ((debug)
                        ? Integer.toString(currentRoom)
                        : ""));

        width = ((Long) room.get("width")).intValue();
        height = ((Long) room.get("height")).intValue();
        System.out.println(width+""+height);

        tile = new JButton[width][height];
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
            tile[x][y].setText((debug) ? Long.toString((Long) j.get("to")) : (String) j.get("label"));
            tile[x][y].addActionListener(l -> {
                currentRoom = ((Long) j.get("to")).intValue();
                renderer((JSONObject) rooms.get(currentRoom));
            });
        }
        if (currentRoom == 0) {
            tile[0][0].setText("Debug");
            tile[0][0].addActionListener(l -> {
                debug = Boolean.logicalXor(debug, true);
                renderer((JSONObject) rooms.get(currentRoom));
            });
        }

        window.setSize(width*100, height*100);
        window.setLayout(new GridLayout(height,width));
        window.revalidate();
        window.repaint();
    }
}
