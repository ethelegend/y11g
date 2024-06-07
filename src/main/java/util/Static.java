package util;

import oop.entity.Entity;
import oop.entity.Warrior;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.GridLayout;

public class Static { // Dedicated class to contain static window and player objects
    public static JFrame window = new JFrame();
    public static JMenuBar menu = new JMenuBar();
    public static Entity player = new Warrior();
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
}
