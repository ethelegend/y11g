package oop.entity;

import oop.weapon.Weapon;

public class Entity {
    public String name; // Name of the entity, not used by player
    public int ac;
    public int speed;
    public int hp;
    public int maxHP;
    public Weapon[] weapons;
    public int pos;
    public int gold;
    public boolean canMove(int pos) { return (Math.abs(this.pos - pos) <= this.speed); } // Returns if the position is within the player's movement range
}