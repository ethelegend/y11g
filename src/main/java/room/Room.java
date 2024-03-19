package room;
import javax.swing.*;
import org.json.simple.*;

public class Room {
    public Room(){}
    public String EmptyRoom(JFrame window, JSONArray rooms) {
        String room0 = (String) ((JSONObject) rooms.get(0)).get("title");
        return room0;
    }
}
