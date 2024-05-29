package oop.entity;

import oop.weapon.Weapon;
import main.Window;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Container;
import java.awt.GridLayout;

public class Entity {
    public String name;
    public int ac;
    public int speed;
    public int hp;
    public int maxHP;
    public Weapon[] weapons;
    public int pos;
    public int gold;
    public boolean canMove(int target) {
        return (Math.abs(this.pos - target) <= this.speed);
    }
}