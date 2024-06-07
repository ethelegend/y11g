/*
    This is the Entity class. It contains all the information required for an entity, plus a function to verify if a position is within movement range
    This is a parent class for 5 individual classes, each one being a constructor for a specific kind of monster. After construction, each object is treated as a generic Entity.
*/

package oop.entity;

import oop.weapon.Weapon;

public class Entity {
    public String name; // Name of the entity, not used by player
    public int ac; // Armour class (i.e. how high you need to roll to successfully attack it)
    public int speed; // How many feet it can move on a specific turn
    public int hp; // Current health points
    public int maxHP; // Original / maximum health points
    public Weapon[] weapons; // Multi-layered object-oriented programming
    public int pos; // Current position
    public int gold; // Current gold
    public boolean canMove(int pos) { return (Math.abs(this.pos - pos) <= this.speed); } // Returns whether the position is within its movement range
}