package main;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.GridLayout;

public class Window { // Dedicated class to contain static JFrame and default layout
    public static JFrame window;

    public Window() {
        window = new JFrame();
        window.setVisible(true);
        window.toFront();
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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
