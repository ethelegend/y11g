package main;

import javax.swing.*;
import java.awt.*;

public class Window {
    public static JFrame window;

    public Window() {
        window = new JFrame();
        window.setVisible(true);
        window.toFront();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
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
        window.revalidate();
        window.repaint();
    }
}
