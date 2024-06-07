/*
    This is my dedicated static class, made to hold all the static elements of my project.
    The most important static element is obviously the main class, followed closely by the JFrame, with the player object and the dice simulation code tied for 3rd.
    The reason I have done this is that the JFrame and the player are required by both main.Main and main.Attack, and this allows me to avoid passing the objects around constantly through fields.
    This has also allowed me to create the infoPopup function, that creates a customisable info popup template.
*/

package main;

// Other classes
import oop.entity.Entity;
import oop.entity.Warrior;

// Related to reading the JSON file
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

// Swing stuff
import javax.swing.*;
import java.awt.GridLayout;
import java.io.FileReader;
import java.io.IOException;

public class Static {
    public static JFrame window = new JFrame();
    public static JMenuBar menu = new JMenuBar();
    public static Entity player = new Warrior();
    public static void main(String[] args) {
        // Initialises the JFrame and JMenuBar
        Static.window.setVisible(true);
        Static.window.toFront();
        Static.window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Adds quit action
        JMenuItem quit = new JMenuItem("Quit");
        quit.addActionListener(l -> {
            JButton[] b = new JButton[]{new JButton("OK")};
            b[0].addActionListener(m -> System.exit(0));
            Static.infoPopup("You collected a total of " + Static.player.gold + " gold!", b);
        });
        Static.menu.add(quit);

        // Adds look action and NZ reference
        JMenuItem look = new JMenuItem("Look");
        look.addActionListener(l -> {
            JFrame window = new JFrame();
            window.setVisible(true);
            window.toFront();
            window.add(new JLabel("You see a room. There is a silver fern growing out of a storm drain.",SwingConstants.CENTER));
            window.setSize(400, 100);
        });
        Static.menu.add(look);

        // Adds place action
        JMenuItem place = new JMenuItem("Place"); // PLACE
        place.addActionListener(l -> {
            JFrame window = new JFrame();
            window.setVisible(true);
            window.toFront();
            if (Static.player.gold == 0) {
                window.add(new JLabel("You don't have anything to place right now.",SwingConstants.CENTER));
                window.setSize(300, 100);
            } else {
                window.add(new JLabel("You place a coin on the ground. Unfortunately it rolls into a storm drain.",SwingConstants.CENTER));
                window.setSize(450, 100);
                Static.player.gold -= 1;
            }
        });
        Static.menu.add(place);

        // Parses the JSON file
        JSONParser parser = new JSONParser(); // Parses rooms.json
        JSONArray rooms = new JSONArray(); // Stores rooms.json
        try {
            rooms = (JSONArray) ((JSONObject) parser.parse(new FileReader("src/main/resources/rooms.json"))).get("rooms");  // The one annoying thing about org.json.simple is that the default child object is Object and not JSONObject
        } catch (IOException e) { // The JSON file could not be found
            System.err.println("Unable to find level data");
            System.exit(404);
        } catch (ParseException e) { // The JSON file could not be read
            System.err.println("Unable to parse level data");
            System.exit(500);
        }
        // Calls main
        new Main(rooms);
    }
    public static void infoPopup(String message, JButton[] button){ // This template is used in both main.Main and main.Attack, so it makes more sense to put it here
        window.getContentPane().removeAll();
        window.setJMenuBar(menu);
        window.add(new JLabel(message,SwingConstants.CENTER)); // Adds the message

        JPanel buttons = new JPanel(); // Container for buttons so GridLayout thinks its 1 object and works correctly
        window.add(buttons);
        buttons.setLayout(new GridLayout()); // So that buttons are ordered horizontally, not vertically
        for (JButton b:button) {
            buttons.add(b);
        }

        window.setLayout(new GridLayout(2,1));
        window.setSize((int) window.getPreferredSize().getWidth() + 10, 200 + menu.getHeight());
    }

    public static int rollDice(int base, int[] dice) {
        int result = base; // Adds the base
        for (int i : dice) {
            result += (int) Math.floor(Math.random()*i+1); // Rolls each dice
        }
        return result; // Returns result
    }
    public static boolean rollCheck(int base, int check) {
        int result = (int) Math.floor(Math.random()*20+1); // Roll a d20
        if (result == 1) {
            return false; // Nat 1
        } if (result == 20) {
            return true; // Nat 20
        } return (result + base >= check);
    }
}