package main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import room.*;

import javax.swing.*;
import java.io.FileReader;
import java.io.IOException;

public class Window {
    JFrame window;
    JSONArray rooms;
    Room room;
    JSONObject currentRoom;
    String[] data = {"0"};
    public Window() {}
    public void RunWindow() {

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
        room = new Room();

        while (true) {
            currentRoom = (JSONObject) rooms.get(Integer.parseInt(data[0]));

            data = room.RoomHandler(currentRoom);
            System.out.println(data[0] + data[1]);

            break;
        }
    }
}
