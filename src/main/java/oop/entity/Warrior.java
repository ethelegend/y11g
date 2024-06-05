package oop.entity;

import main.Dice;
import oop.weapon.Crossbow;
import oop.weapon.Greatsword;
import oop.weapon.Weapon;


public class Warrior extends Entity {
    public Warrior() {
        this.name = "Warrior";
        this.ac = 18;
        this.speed = 30;
        this.hp = 16; // Technically should be 4 + 2d10, but im using fixed hp so its not unfair
        this.maxHP = this.hp;
        this.weapons = new Weapon[]{
                new Crossbow(4,2),
                new Greatsword(5,3)
        };
    }
}

