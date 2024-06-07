package util;

import main.Main; // The actual main class
// Types of JSON objects
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
// JSON parsing code
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
public class Initialiser { // Houses the executable class and parses the room file
    public static void main(String[] args) {
        JSONParser parser = new JSONParser(); // Parses rooms.json
        JSONArray rooms = new JSONArray(); // Stores rooms.json
        try {
            rooms = (JSONArray) ((JSONObject) parser.parse(new FileReader("src/main/resources/rooms.json"))).get("rooms"); // These kinds of type casting are quite common
        } catch (IOException e) { // The JSON file could not be found
            System.err.println("Unable to find level data");
            System.exit(404);
        } catch (ParseException e) { // The JSON file could not be read
            System.err.println("Unable to parse level data");
            System.exit(500);
        }
        new Main(rooms);

    }

}
