package oop.entity;

import oop.weapon.Weapon;

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