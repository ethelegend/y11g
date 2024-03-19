package main;

import things.*;

import javax.swing.*;

public class Window {
    JFrame window;
    public Window() {
        window = new JFrame();

        window.setVisible(true);
        window.toFront();

        EmptyRoom emptyRoom = new EmptyRoom();
        window.setTitle(emptyRoom.EmptyRoom(window));
    }
}
