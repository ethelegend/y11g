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
        renderer();
    }
    private void renderer() {
        JSONObject room = (JSONObject) rooms.get(currentRoom);
        System.out.println((boolean) ((JSONObject) rooms.get(currentRoom)).get("explored"));
        boolean explored = (boolean) room.put("explored", true);

        window.getContentPane().removeAll();
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
                renderer();
            });
        }
        if (currentRoom == 0) {
            tile[0][0].setText("Toggle Debug");
            tile[0][0].addActionListener(l -> {
                debug = Boolean.logicalXor(debug, true);
                renderer();
            });
        }

        window.setLayout(new GridLayout(height,width));
        window.setSize(width*100, height*100);
        window.revalidate();
        window.repaint();

    }
}