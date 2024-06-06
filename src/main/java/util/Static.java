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
    public static JMenuBar quit = new JMenuBar();
    public static Entity player = new Warrior();
    public static void infoPopup(String labelText, JButton[] button){
        window.getContentPane().removeAll();
        window.add(new JLabel(labelText,SwingConstants.CENTER));

        JPanel buttons = new JPanel();
        window.add(buttons);
        buttons.setLayout(new GridLayout());
        for (JButton b:button) {
            buttons.add(b);
        }


        window.setLayout(new GridLayout(2,1));
        window.setSize((int) window.getPreferredSize().getWidth() + 10, 200);
        window.setJMenuBar(quit);
        window.revalidate();
        window.repaint();
    }
}
