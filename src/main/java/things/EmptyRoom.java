package things;
import javax.swing.*;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class EmptyRoom {
    public EmptyRoom(){}
    public String EmptyRoom(JFrame window) {
        JSONParser parser = new JSONParser();
        JSONObject roomFile;
        try {
            roomFile = (JSONObject) parser.parse(new FileReader("src/main/resources/rooms.json"));
        }catch (IOException | ParseException dExceptionzNutz) {
            return null;
        }
        JSONArray rooms = (JSONArray) roomFile.get("rooms");
        String room0 = (String) ((JSONObject) rooms.get(0)).get("title");
        return room0;
    }
}
