package room;
import javax.swing.*;

import main.Window;
import org.json.simple.*;

import java.awt.*;

public class Room {
    JFrame window;
    String[] data = {"0","0"};
    int width;
    int height;
    JButton[][] tile;

    public Room(){
        window = new JFrame();
        window.setVisible(true);
        window.toFront();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public String[] RoomHandler(JSONObject room) {
        window.setTitle((String) room.get("title"));

        width = ((Long) room.get("width")).intValue();
        height = ((Long) room.get("height")).intValue();
        System.out.println(width+""+height);

        tile = new JButton[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                tile[i][j] = new JButton();
                window.add(tile[i][j]);
            }
        }
        window.setSize(width*100, height*100);
        window.setLayout(new GridLayout(height,width));

        return data;
    }
}
