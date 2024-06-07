package oop.entity;

import oop.weapon.Crossbow;
import oop.weapon.Greatsword;
import oop.weapon.Weapon;


public class Warrior extends Entity {
    public Warrior() {
        this.name = "Warrior";
        this.ac = 18;
        this.speed = 30;
        this.hp = 24; // Technically should be 2d10+4, but since I haven't added a way to heal yet I'm being generous
        this.maxHP = this.hp;
        this.weapons = new Weapon[]{
                new Crossbow(4,2),  // +4 to hit, 1d10+2
                new Greatsword(5,3) // +5 to hit, 2d6+3
        };
    }
}