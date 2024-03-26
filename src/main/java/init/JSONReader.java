package init;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import main.*;

import javax.swing.*;
import java.io.FileReader;
import java.io.IOException;

public class JSONReader {
    JSONArray rooms;
    public JSONReader() {
        JSONParser parser = new JSONParser();
        try {
            rooms = (JSONArray) ((JSONObject) parser.parse(new FileReader("src/main/resources/rooms.json"))).get("rooms");
        }catch (IOException e) {
            System.err.println("Unable to find level data");
            System.exit(1);
        } catch (ParseException e) {
            System.err.println("Unable to parse level data");
            System.exit(2);
        }
        new Main(rooms);

    }
}
