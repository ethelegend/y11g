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
    public Window() {
        window = new JFrame();

        window.setVisible(true);
        window.toFront();

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
        window.setTitle(room.EmptyRoom(window,rooms));
    }
}
