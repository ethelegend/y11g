package oop.entity;

import main.Dice;
import oop.weapon.Longbow;
import oop.weapon.Longsword;
import oop.weapon.Weapon;


public class Warrior extends Entity {
    public Warrior() {
        this.name = "Warrior";
        this.ac = 18;
        this.speed = 30;
        this.hp = Dice.rollDice(2,new int[]{8,8});
        this.maxHP = this.hp;
        this.weapons = new Weapon[]{
                new Longsword(4,1),
                new Longbow(4,1)
        };
    }
}

